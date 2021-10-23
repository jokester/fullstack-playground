package io.jokester.fullstack_playground.db.generated

import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.OffsetDateTime

class RoomSpec extends FixtureAnyFlatSpec with Matchers with AutoRollback {

  behavior of "Room"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Room.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Room.findBy(sqls"room_id = ${123}")
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Room.findAll()
    allResults.size should be > (0)
  }
  it should "count all records" in { implicit session =>
    val count = Room.countAll()
    count should be > (0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Room.findAllBy(sqls"room_id = ${123}")
    results.size should be > (0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Room.countBy(sqls"room_id = ${123}")
    count should be > (0L)
  }
  it should "create new record" in { implicit session =>
    val created = Room.create(name = "MyString", createdAt = null)
    created should not be (null)
  }
  it should "save a record" in { implicit session =>
    val entity = Room.findAll().head
    // TODO modify something
    val modified = entity
    val updated  = Room.save(modified)
    updated should not equal (entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity  = Room.findAll().head
    val deleted = Room.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Room.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Room.findAll()
    entities.foreach(e => Room.destroy(e))
    val batchInserted = Room.batchInsert(entities)
    batchInserted.size should be > (0)
  }
}
