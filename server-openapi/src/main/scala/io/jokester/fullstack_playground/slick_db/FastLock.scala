package io.jokester.fullstack_playground.slick_db

import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.Database
import slick.lifted.Query
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.TransactionIsolation

import scala.concurrent.{ExecutionContext, Future}

sealed trait LockFailure
case object LockInUse     extends LockFailure
case object NothingToLock extends LockFailure

sealed trait LockResult[T]
case class LockFailed[T](reason: LockFailure) extends LockResult[T]
case class LockAcquired[T](result: T)         extends LockResult[T]

// taken from https://github.com/slick/slick/issues/1870
case class FastLock[E, U](selectQuery: Query[E, U, Seq])(implicit
    db: Database,
    ec: ExecutionContext,
) {
  def apply[R](effect: U => DBIO[R]): Future[LockResult[R]] =
    retryFailedLock(attemptLock(effect))

  private def attemptLock[R](effect: U => DBIO[R]): Future[LockResult[R]] = {
    val s: DBIO[LockResult[R]] =
      (for {
        element <- forUpdateNoWait
        result <- element match {
          case Some(row) => effect(row).map(LockAcquired[R](_))
          case _         => DBIO.successful(LockFailed[R](NothingToLock))
        }
      } yield {
        result
      })

    db.run(s.withTransactionIsolation(TransactionIsolation.RepeatableRead))
  }

  private def retryFailedLock[R](result: Future[LockResult[R]]): Future[LockResult[R]] =
    ???
//    result.recover { case PgSqlState.LockNotAvailable() =>
//      LockFailed(LockInUse)
//    }

  private def forUpdateNoWait: DBIO[Option[U]] =
    ???
//    lockAction.flatMap(_ => query.result.headOption)

  private def lockAction: DBIO[Option[Int]] =
    sql"SELECT 1 FROM (#$baseSql) base FOR UPDATE NOWAIT".as[Int].headOption

  private val baseSql = selectQuery.result.statements.head

}
