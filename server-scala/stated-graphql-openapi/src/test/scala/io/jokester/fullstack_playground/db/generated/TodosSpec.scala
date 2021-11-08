package io.jokester.fullstack_playground.db.generated

import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{OffsetDateTime}


class TodosSpec extends FixtureAnyFlatSpec with Matchers with AutoRollback {
  

  behavior of "Todos"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Todos.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Todos.findBy(sqls"todo_id = ${123}")
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Todos.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Todos.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Todos.findAllBy(sqls"todo_id = ${123}")
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Todos.countBy(sqls"todo_id = ${123}")
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Todos.create(title = "MyString", desc = "MyString", createdAt = null, updatedAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Todos.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Todos.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Todos.findAll().head
    val deleted = Todos.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Todos.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Todos.findAll()
    entities.foreach(e => Todos.destroy(e))
    val batchInserted = Todos.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
