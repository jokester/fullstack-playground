package io.jokester.fullstack_playground.quill

import io.getquill.MappedEncoding
import org.postgresql.jdbc.TimestampUtils

import java.time.OffsetDateTime
import java.util.TimeZone

trait QuillDatetimeEncoding {
  private val pgTimestampUtils = new TimestampUtils(true, () => TimeZone.getTimeZone("UTC"))
  implicit val offsetDatetimeDecoder: MappedEncoding[String, OffsetDateTime] = {
    MappedEncoding[String, OffsetDateTime](pgTimestampUtils.toOffsetDateTime)
  }
  implicit val offsetDatetimeEncoder: MappedEncoding[OffsetDateTime, String] =
    MappedEncoding[OffsetDateTime, String](pgTimestampUtils.toString)
}
