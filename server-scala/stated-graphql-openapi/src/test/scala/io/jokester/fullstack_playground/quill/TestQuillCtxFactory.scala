package io.jokester.fullstack_playground.quill

object TestQuillCtxFactory {
  def createTestContext: QuillCtxFactory.OurCtx = QuillCtxFactory.createContext("database.test")

  def withTestContext(
      testCase: (
          QuillCtxFactory.OurCtx,
      ) => Unit,
  ): Unit = {
    val created = createTestContext
    try {
      testCase(created)
    } finally {
      created.close()
    }
  }
}
