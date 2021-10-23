package io.jokester.fullstack_playground.db.generated

import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MessageSpec extends FixtureAnyFlatSpec with Matchers with AutoRollback {

  behavior of "Message"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Message.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Message.findBy(sqls"message_id = ${1L}")
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Message.findAll()
    allResults.size should be > (0)
  }
  it should "count all records" in { implicit session =>
    val count = Message.countAll()
    count should be > (0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Message.findAllBy(sqls"message_id = ${1L}")
    results.size should be > (0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Message.countBy(sqls"message_id = ${1L}")
    count should be > (0L)
  }
  it should "create new record" in { implicit session =>
    val created = Message.create(userId = 123, roomId = 123, content = "MyString")
    created should not be (null)
  }
  it should "save a record" in { implicit session =>
    val entity = Message.findAll().head
    // TODO modify something
    val modified = entity
    val updated  = Message.save(modified)
    updated should not equal (entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity  = Message.findAll().head
    val deleted = Message.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Message.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Message.findAll()
    entities.foreach(e => Message.destroy(e))
    val batchInserted = Message.batchInsert(entities)
    batchInserted.size should be > (0)
  }
}
