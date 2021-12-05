package io.jokester.fullstack_playground.user_todolist_api

import io.jokester.fullstack_playground.user_todolist_api.UserTodoApi.AuthSuccess
import io.jokester.http_api.FailableValues
import org.scalatest.EitherValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import UserTodoApi._

trait UserTodoServiceTest
    extends should.Matchers
    with EitherValues
    with ScalaFutures
    with FailableValues {
  self: AnyFlatSpec =>

  def testee: UserTodoService

  "testee" should "create users" in {

    val newUser1 = testee.createUser(
      UserTodoApi.CreateUserRequest(
        email = "",
        initialPass = "123456",
        profile = UserProfile(nickname = None, avatarUrl = None),
      ),
    )

    newUser1.right.value shouldBe a[AuthSuccess]

  }

}
