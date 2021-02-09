package io.jokester.fullstack_playground.genereated_scalikejdbc

import io.jokester.fullstack_playground.scalikejdbc_db.ScalikeJDBCConnection
import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback

class TodoSpec extends FixtureAnyFlatSpec with Matchers with AutoRollback {

  lazy val dbInited = ScalikeJDBCConnection.initNamed()

  override def db = {
    dbInited
    ScalikeJDBCConnection.db2().toDB()
  }

  behavior of "Todo"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Todo.find(1)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Todo.findBy(sqls"todo_id = ${1}")
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Todo.findAll()
    allResults.size should be > (0)
  }
  it should "count all records" in { implicit session =>
    val count = Todo.countAll()
    count should be > (0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Todo.findAllBy(sqls"todo_id = ${1}")
    results.size should be > (0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Todo.countBy(sqls"todo_id = ${1}")
    count should be > (0L)
  }
  it should "create / update / remove a record" in { implicit session =>
    val created =
      Todo.create(title = "MyString", desc = "MyString")
    created should not be (null)

    val loaded   = Todo.find(created.todoId).get
    val modified = loaded.copy(title = "new Title")
    val updated  = Todo.save(modified)
    updated should not equal (loaded)
    updated should equal(modified)

    val deleted = Todo.destroy(created)
    deleted should be(1)
    val shouldBeNone = Todo.find(created.todoId)
    shouldBeNone.isDefined should be(false)
  }

  it should "perform batch insert" in { implicit session =>
    val entities = Todo.findAll()
    entities.foreach(e => Todo.destroy(e))
    val batchInserted = Todo.batchInsert(entities)
    batchInserted.size should be > (0)
  }
}
