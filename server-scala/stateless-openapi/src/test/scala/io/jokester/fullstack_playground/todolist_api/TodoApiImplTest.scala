package io.jokester.fullstack_playground.todolist_api
import org.scalatest._
import flatspec._
import matchers._

trait TodoApiImplTest extends should.Matchers {
  self: AnyFlatSpec =>
  import io.jokester.fullstack_playground.todolist_api.TodoApi._

  def testee: TodoApiImpl

  "testee" should "CRUD" in {
    val created = testee.create(CreateTodoIntent("title", "desc"))
  }

}
