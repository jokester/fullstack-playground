package io.jokester.fullstack_playground.genereated_scalikejdbc

import io.jokester.fullstack_playground.scalikejdbc_db.{TodoRepo, UserRepo}
import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalikejdbc.scalatest.AutoRollback

import java.time.OffsetDateTime

class TodoRepoSpec
    extends FixtureAnyFlatSpec
    with Matchers
    with AutoRollback
    with TestDbAndRepository
    with TestData {

  it should "create update and remove todo item" in { () =>
    db().localTx(implicit session => {

      val userRepo = UserRepo()
      val todoRepo = TodoRepo()

      val testUser =
        userRepo
          .createUser(faker.internet().emailAddress(), faker.internet().password(), UserProfile())

      val newTodo = todoRepo.createTodo(testUser, faker.animal().name(), faker.chuckNorris().fact())

      newTodo shouldBe a[Todo]

      val updatedTodo = newTodo.copy(finishedAt = Some(OffsetDateTime.now()))
      val saved       = todoRepo.updateTodo(updatedTodo)

      saved should equal(updatedTodo)

    })
  }

}
