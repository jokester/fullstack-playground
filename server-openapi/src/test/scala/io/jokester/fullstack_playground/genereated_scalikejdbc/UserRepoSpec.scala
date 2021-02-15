package io.jokester.fullstack_playground.genereated_scalikejdbc

import io.jokester.fullstack_playground.scalikejdbc_db.UserRepo
import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalikejdbc.scalatest.AutoRollback

class UserRepoSpec
    extends FixtureAnyFlatSpec
    with Matchers
    with AutoRollback
    with TestDbAndRepository
    with TestData {

  it should "create, update and remove user" in { () =>
    db().localTx(implicit session => {

      val testee = UserRepo()

      val created = testee
        .createUser(
          faker.internet.emailAddress(),
          "string",
          UserProfile(
            nickname = Some(faker.name().nameWithMiddle()),
            avatarUrl = Some(faker.internet().avatar()),
          ),
        )
      created shouldBe a[User]

      val updated = testee.updateUser(created.copy(userPassword = "456"))
      updated.userPassword should equal("456")

      testee.removeUser(created) shouldBe a[User]
    })

  }
}
