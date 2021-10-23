package io.jokester.fullstack_playground.db.generated

import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.OffsetDateTime

class TodoappTodosSpec extends FixtureAnyFlatSpec with Matchers with AutoRollback {

  behavior of "TodoappTodos"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = TodoappTodos.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = TodoappTodos.findBy(sqls"todo_id = ${1L}")
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = TodoappTodos.findAll()
    allResults.size should be > (0)
  }
  it should "count all records" in { implicit session =>
    val count = TodoappTodos.countAll()
    count should be > (0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = TodoappTodos.findAllBy(sqls"todo_id = ${1L}")
    results.size should be > (0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = TodoappTodos.countBy(sqls"todo_id = ${1L}")
    count should be > (0L)
  }
  it should "create new record" in { implicit session =>
    val created = TodoappTodos.create(
      userId = 123,
      title = "MyString",
      description = "MyString",
      createdAt = null,
      updatedAt = null,
    )
    created should not be (null)
  }
  it should "save a record" in { implicit session =>
    val entity = TodoappTodos.findAll().head
    // TODO modify something
    val modified = entity
    val updated  = TodoappTodos.save(modified)
    updated should not equal (entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity  = TodoappTodos.findAll().head
    val deleted = TodoappTodos.destroy(entity)
    deleted should be(1)
    val shouldBeNone = TodoappTodos.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = TodoappTodos.findAll()
    entities.foreach(e => TodoappTodos.destroy(e))
    val batchInserted = TodoappTodos.batchInsert(entities)
    batchInserted.size should be > (0)
  }
}
