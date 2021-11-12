package io.jokester.fullstack_playground.quill
import org.scalatest._
import flatspec._
import matchers._

class QuillTodoApiImplTest extends AnyFlatSpec with should.Matchers {

  "QuillTodoApiImpl" should "CRUD" in {
    TestQuillCtxFactory.withTestContext(ctx => {

      val testee = new QuillTodoApiImpl(ctx)

      val initial = testee.list()

    })

  }

}
