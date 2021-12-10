package io.jokester.fullstack_playground.user_todolist_api

import com.github.javafaker.Faker
import io.jokester.fullstack_playground.user_todolist_api.UserTodoApi._
import io.jokester.http_api.FailableValues
import io.jokester.http_api.OpenAPIConvention.BadRequest
import org.scalatest.{EitherValues, OptionValues}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

trait UserTodoServiceTest
    extends should.Matchers
    with EitherValues
    with OptionValues
    with ScalaFutures
    with FailableValues {
  self: AnyFlatSpec =>

  private val faker = new Faker

  def testee: UserTodoService

  "testee" should "create and auth users" in {

    val createUserRequest = UserTodoApi.CreateUserRequest(
      email = faker.internet().emailAddress(),
      initialPass = faker.internet().password(),
      profile = UserProfile(
        nickname = Some(faker.chuckNorris().fact()),
        avatarUrl = Some(faker.internet().image()),
      ),
    )
    val newUser1 = testee.createUser(
      createUserRequest,
    )

    newUser1.right.value shouldBe a[UserAccount]

    {
      val loginSuccess = testee.loginUser(
        LoginRequest(email = createUserRequest.email, password = createUserRequest.initialPass),
      )

      loginSuccess.right.value shouldBe a[UserAccount]
      loginSuccess.right.value.profile.nickname.get should equal(
        createUserRequest.profile.nickname.get,
      )
      loginSuccess.right.value.profile.avatarUrl should equal(createUserRequest.profile.avatarUrl)
    }

    {
      val passwordMismatch = testee.loginUser(
        LoginRequest(email = createUserRequest.email, password = faker.internet().password()),
      )
      passwordMismatch.left.value should equal(BadRequest("invalid cred"))
    }

    {
      val userNotFound = testee.loginUser(
        LoginRequest(
          email = faker.internet().emailAddress(),
          password = createUserRequest.initialPass,
        ),
      )
      userNotFound.left.value should equal(BadRequest("invalid cred"))
    }

  }

}
