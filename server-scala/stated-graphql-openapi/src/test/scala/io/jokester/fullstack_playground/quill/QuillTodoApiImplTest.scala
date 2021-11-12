package io.jokester.fullstack_playground.quill
import org.scalatest._
import flatspec._
import matchers._

class QuillTodoApiImplTest extends AnyFlatSpec with should.Matchers {
  import io.jokester.fullstack_playground.todolist_api.TodoApi._

  "QuillTodoApiImpl" should "CRUD" in {
    TestQuillCtxFactory.withTestContext(ctx => {

      val testee = new QuillTodoApiImpl(ctx)

      val initial = testee.list()

      val created = testee.create(TodoCreateRequest("title", "desc"))

    })

  }

}
