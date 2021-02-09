package io.jokester.fullstack_playground.slick_db

import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.Database
import slick.lifted.Query
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.TransactionIsolation

object PgPermLock {
  implicit class SelectQueryWithLock[T, E](selectQuery: Query[T, E, Seq]) {

    def lockNoWait(): Query[T, E, Seq] = {
      ???

    }

    private def rebuiltQuery(): Query[T, E, Seq] = {

      if (selectQuery.result.statements.size != 1) {
        throw new IllegalArgumentException("expected a single SELECT query")
      }

      ???

    }

  }

}
