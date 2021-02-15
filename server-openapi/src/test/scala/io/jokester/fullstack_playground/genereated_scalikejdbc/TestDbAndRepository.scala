package io.jokester.fullstack_playground.genereated_scalikejdbc

import io.jokester.fullstack_playground.scalikejdbc_db.ScalikeJDBCConnection
import scalikejdbc.DB
import scalikejdbc.scalatest.AutoRollback

trait TestDbAndRepository {
  self: AutoRollback =>

  override def db(): DB = ScalikeJDBCConnection.namedDB()
}
