package io.jokester.fullstack_playground.genereated_scalikejdbc

import io.circe.{Encoder, Json}
import io.jokester.fullstack_playground.scalikejdbc_db.ScalikeJDBCConnection
import org.postgresql.util.PGobject
import scalikejdbc._

import java.time.OffsetDateTime

case class UserProfile() {}

object UserProfile {
  implicit val circeEncoder: Encoder[UserProfile] = Encoder(whatever => Json.Null)
}

case class User(
    userId: Int,
    userEmail: String,
    userPassword: String,
    userProfile: /* FIXME */ Any,
    createdAt: Option[OffsetDateTime] = None,
    updatedAt: Option[OffsetDateTime] = None,
)

object User extends SQLSyntaxSupport[User] {

  override val schemaName = Some("public")

  override val tableName = "user"

  override val columns =
    Seq("user_id", "user_email", "user_password", "user_profile", "created_at", "updated_at")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User =
    User(
      userId = rs.get(u.userId),
      userEmail = rs.get(u.userEmail),
      userPassword = rs.get(u.userPassword),
      userProfile = rs.any(u.userProfile),
      createdAt = rs.get(u.createdAt),
      updatedAt = rs.get(u.updatedAt),
    )

  val user = User.syntax("u")

//  override val autoSession = AutoSession
  override val connectionPoolName = ScalikeJDBCConnection.poolName

}
