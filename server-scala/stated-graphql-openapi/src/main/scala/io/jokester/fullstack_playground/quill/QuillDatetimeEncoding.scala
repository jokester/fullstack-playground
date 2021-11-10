package io.jokester.fullstack_playground.quill

import io.getquill.MappedEncoding

import java.time.OffsetDateTime

trait QuillDatetimeEncoding {
  implicit val offsetDatetimeDecoder: MappedEncoding[String, OffsetDateTime] =
    MappedEncoding[String, OffsetDateTime](OffsetDateTime.parse(_))
  implicit val offsetDatetimeEncoder: MappedEncoding[OffsetDateTime, String] =
    MappedEncoding[OffsetDateTime, String](_.toString)
}
