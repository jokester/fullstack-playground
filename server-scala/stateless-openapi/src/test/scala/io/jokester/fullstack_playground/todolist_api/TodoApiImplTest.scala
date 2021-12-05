package io.jokester.fullstack_playground.todolist_api

import io.jokester.http_api.FailableValues
import org.scalatest._
import flatspec._
import matchers._
import org.scalatest.concurrent.ScalaFutures

trait TodoApiImplTest
    extends should.Matchers
    with EitherValues
    with ScalaFutures
    with FailableValues {
  self: AnyFlatSpec =>
  import io.jokester.fullstack_playground.todolist_api.TodoApi._
  import io.jokester.http_api.OpenAPIConvention._

  def testee: TodoApiService
  "testee" should "CRUD" in { // C
    val list1   = testee.list().right.value.todos
    val created = testee.create(CreateTodoIntent("title", "desc")).asFuture.futureValue.right.value
    list1 should not.contain(created)

    val list2 = testee.list().right.value.todos
    list2 should contain(created)

    // R
    testee.show(created.id).right.value should equal(created)
    testee.show(-2).left.value should equal(NotFound(s"Todo(id=-2) not found"))

    // U
    testee.update(-3, created).left.value should equal(NotFound(s"Todo(id=-3) not found"))
    val updated1 =
      testee
        .update(created.id, created.copy(title = "title1", desc = "desc1"))
        .right
        .value
    val updated2 =
      testee
        .update(created.id, created.copy(title = "title2", desc = "desc2", finished = true))
        .right
        .value
    val updated3 =
      testee
        .update(created.id, created.copy(title = "title3", desc = "desc3", finished = false))
        .right
        .value
    val updated4 =
      testee
        .update(created.id, created.copy(title = "title4", desc = "desc4"))
        .right
        .value
    val updated = updated4
    testee.list().right.value.todos should not.contain(updated1)
    testee.list().right.value.todos should contain(updated)
    testee.list().right.value.todos should contain(updated)
    testee.list().right.value.todos should not.contain(created)

    // D
    testee.remove(created.id).right.value should equal(updated)
    testee.remove(-1).left.value should equal(NotFound(s"Todo(id=-1) not found"))
    val list3 = testee.list().right.value.todos
    list3 should not.contain(created)
    list3 should not.contain(updated)

  }

}
