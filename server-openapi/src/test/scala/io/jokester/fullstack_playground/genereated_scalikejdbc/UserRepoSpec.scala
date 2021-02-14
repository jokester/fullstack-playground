package io.jokester.fullstack_playground.genereated_scalikejdbc

import io.jokester.fullstack_playground.scalikejdbc_db.{ScalikeJDBCConnection, UserRepo}
import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalikejdbc.scalatest.AutoRollback

class UserRepoSpec extends FixtureAnyFlatSpec with Matchers with AutoRollback {

  lazy val dbInited = ScalikeJDBCConnection.initNamed()

  def testDB = {
    dbInited
    ScalikeJDBCConnection.db2
  }

  it should "create user" in { () =>
    testDB.localTx(implicit session => {

      val testee = new UserRepo()
      testee.createUser("email", "string") should equal(new Object())
    })

  }
}
