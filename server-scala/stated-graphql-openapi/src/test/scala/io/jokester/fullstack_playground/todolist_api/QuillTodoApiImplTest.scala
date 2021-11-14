package io.jokester.fullstack_playground.todolist_api

import io.jokester.fullstack_playground.quill.TestQuillCtxFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec._

class QuillTodoApiImplTest extends AnyFlatSpec with TodoApiImplTest with BeforeAndAfterAll {

  private val testCtx = TestQuillCtxFactory.createTestContext
  override val testee = new QuillTodoApiImpl(testCtx)

  override protected def afterAll(): Unit = {
    testCtx.close()
  }
}
