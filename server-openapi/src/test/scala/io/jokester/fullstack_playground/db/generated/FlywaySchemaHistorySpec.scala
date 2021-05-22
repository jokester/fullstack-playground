package io.jokester.fullstack_playground.db.generated

import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{OffsetDateTime}

class FlywaySchemaHistorySpec extends FixtureAnyFlatSpec with Matchers with AutoRollback {

  behavior of "FlywaySchemaHistory"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = FlywaySchemaHistory.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = FlywaySchemaHistory.findBy(sqls"installed_rank = ${123}")
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = FlywaySchemaHistory.findAll()
    allResults.size should be > (0)
  }
  it should "count all records" in { implicit session =>
    val count = FlywaySchemaHistory.countAll()
    count should be > (0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = FlywaySchemaHistory.findAllBy(sqls"installed_rank = ${123}")
    results.size should be > (0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = FlywaySchemaHistory.countBy(sqls"installed_rank = ${123}")
    count should be > (0L)
  }
  it should "create new record" in { implicit session =>
    val created = FlywaySchemaHistory.create(
      installedRank = 123,
      description = "MyString",
      `type` = "MyString",
      script = "MyString",
      installedBy = "MyString",
      installedOn = null,
      executionTime = 123,
      success = false,
    )
    created should not be (null)
  }
  it should "save a record" in { implicit session =>
    val entity = FlywaySchemaHistory.findAll().head
    // TODO modify something
    val modified = entity
    val updated  = FlywaySchemaHistory.save(modified)
    updated should not equal (entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity  = FlywaySchemaHistory.findAll().head
    val deleted = FlywaySchemaHistory.destroy(entity)
    deleted should be(1)
    val shouldBeNone = FlywaySchemaHistory.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = FlywaySchemaHistory.findAll()
    entities.foreach(e => FlywaySchemaHistory.destroy(e))
    val batchInserted = FlywaySchemaHistory.batchInsert(entities)
    batchInserted.size should be > (0)
  }
}
