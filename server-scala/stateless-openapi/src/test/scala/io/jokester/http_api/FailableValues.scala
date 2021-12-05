package io.jokester.http_api

import io.jokester.http_api.OpenAPIConvention.{ApiResultSync, Failable}
import org.scalatest.concurrent.ScalaFutures

import scala.language.implicitConversions

trait FailableValues extends ScalaFutures {

  implicit def summon[T](v: Failable[T]): ApiResultSync[T] = {
    v.asFuture.futureValue
  }

}
