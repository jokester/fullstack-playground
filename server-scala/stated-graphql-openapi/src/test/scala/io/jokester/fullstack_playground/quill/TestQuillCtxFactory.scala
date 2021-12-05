package io.jokester.fullstack_playground.quill

object TestQuillCtxFactory {
  def createTestContext: QuillContextFactory.PublicCtx =
    QuillContextFactory.createPublicContext("database.test")

  def withTestContext(
      testCase: (
          QuillContextFactory.PublicCtx,
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
