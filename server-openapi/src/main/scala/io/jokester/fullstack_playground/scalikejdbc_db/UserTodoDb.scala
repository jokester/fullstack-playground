package io.jokester.fullstack_playground.scalikejdbc_db

import scalikejdbc.{DB, DBSession}

trait UserTodoDb {
  protected def db(): DB                                        = ScalikeJDBCConnection.namedDB()
  protected def userRepo(implicit session: DBSession): UserRepo = UserRepo()
  protected def todoRepo(implicit session: DBSession): TodoRepo = TodoRepo()
}
