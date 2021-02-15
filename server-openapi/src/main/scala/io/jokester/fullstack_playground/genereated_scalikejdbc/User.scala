package io.jokester.fullstack_playground.genereated_scalikejdbc

import io.jokester.fullstack_playground.scalikejdbc_db.{OurBinders, ScalikeJDBCConnection}
import scalikejdbc._

import java.time.OffsetDateTime

case class UserProfile(nickname: Option[String] = None, avatarUrl: Option[String] = None) {}

case class User(
    userId: Int,
    userEmail: String,
    userPassword: String,
    userProfile: UserProfile,
    createdAt: Option[OffsetDateTime] = None,
    updatedAt: Option[OffsetDateTime] = None,
)

object User extends SQLSyntaxSupport[User] with OurBinders {

  override val schemaName = Some("public")

  override val tableName = "user"

  override val columns =
    Seq("user_id", "user_email", "user_password", "user_profile", "created_at", "updated_at")
//  override val autoSession = AutoSession
  override val connectionPoolName = ScalikeJDBCConnection.poolName
  val user                        = User.syntax("u")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)

  def apply(u: ResultName[User])(rs: WrappedResultSet): User =
    User(
      userId = rs.get(u.userId),
      userEmail = rs.get(u.userEmail),
      userPassword = rs.get(u.userPassword),
      userProfile = userProfileBinder(rs.underlying, u.userProfile),
      createdAt = rs.get(u.createdAt),
      updatedAt = rs.get(u.updatedAt),
    )

}
