package io.jokester.fullstack_playground.todolist_api
import org.scalatest._
import flatspec._
import matchers._

trait TodoApiImplTest extends should.Matchers with EitherValues {
  self: AnyFlatSpec =>
  import io.jokester.fullstack_playground.todolist_api.TodoApi._
  import io.jokester.fullstack_playground.todolist_api.ApiConvention._

  def testee: TodoApiImpl

  "testee" should "CRUD" in {
    // C
    val list1   = testee.list().right.value
    val created = testee.create(CreateTodoIntent("title", "desc")).right.value
    list1 should not.contain(created)

    val list2 = testee.list().right.value
    list2 should contain(created)

    // R
    testee.show(created.id).right.value should equal(created)
    testee.show(-2).left.value should equal(NotFound(s"Todo(id=-2) not found"))

    // U
    testee.update(-3, created).left.value should equal(NotFound(s"Todo(id=-3) not found"))
    val updated =
      testee
        .update(created.id, created.copy(title = "title2", desc = "desc2", finished = true))
        .right
        .value
    testee.list().right.value should contain(updated)
    testee.list().right.value should not.contain(created)

    // D
    testee.remove(created.id).right.value should equal(updated)
    testee.remove(-1).left.value should equal(NotFound(s"Todo(id=-1) not found"))
    val list3 = testee.list().right.value
    list3 should not.contain(created)
    list3 should not.contain(updated)

  }

}