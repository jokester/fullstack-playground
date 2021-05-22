package io.jokester.fullstack_playground.db.generated

import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{OffsetDateTime}

class TodoappUsersSpec extends FixtureAnyFlatSpec with Matchers with AutoRollback {

  behavior of "TodoappUsers"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = TodoappUsers.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = TodoappUsers.findBy(sqls"user_id = ${1L}")
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = TodoappUsers.findAll()
    allResults.size should be > (0)
  }
  it should "count all records" in { implicit session =>
    val count = TodoappUsers.countAll()
    count should be > (0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = TodoappUsers.findAllBy(sqls"user_id = ${1L}")
    results.size should be > (0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = TodoappUsers.countBy(sqls"user_id = ${1L}")
    count should be > (0L)
  }
  it should "create new record" in { implicit session =>
    val created = TodoappUsers.create(
      userEmail = "MyString",
      userProfile = null,
      passwordHash = "MyString",
      createdAt = null,
      updatedAt = null,
    )
    created should not be (null)
  }
  it should "save a record" in { implicit session =>
    val entity = TodoappUsers.findAll().head
    // TODO modify something
    val modified = entity
    val updated  = TodoappUsers.save(modified)
    updated should not equal (entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity  = TodoappUsers.findAll().head
    val deleted = TodoappUsers.destroy(entity)
    deleted should be(1)
    val shouldBeNone = TodoappUsers.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = TodoappUsers.findAll()
    entities.foreach(e => TodoappUsers.destroy(e))
    val batchInserted = TodoappUsers.batchInsert(entities)
    batchInserted.size should be > (0)
  }
}
