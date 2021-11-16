package io.jokester.fullstack_playground.quill

trait QuillWorkarounds {

  /**
    * handles `IllegalStateException(Expected a single result but got List())`
    * when
    * query.delete.returing throws when removed row count was not 1
    */
  def handleRemovedRowNotUnique[T](e: Throwable, f: => T): T = {
    e match {
      case ise: java.lang.IllegalStateException
          if ise.getMessage == """Expected a single result but got List()""" =>
        f
      case _ => throw e
    }
  }
}
