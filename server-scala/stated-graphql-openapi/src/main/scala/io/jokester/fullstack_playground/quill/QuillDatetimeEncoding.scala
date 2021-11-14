package io.jokester.fullstack_playground.quill

import com.typesafe.scalalogging.LazyLogging
import io.getquill.MappedEncoding
import org.postgresql.jdbc.TimestampUtils

import java.sql.{PreparedStatement, Types}
import java.time.OffsetDateTime
import java.util.TimeZone

trait QuillDatetimeEncoding extends LazyLogging {
  private val pgTimestampUtils = new TimestampUtils(true, () => TimeZone.getTimeZone("UTC"))
  implicit val offsetDatetimeDecoder: MappedEncoding[String, OffsetDateTime] = {
    MappedEncoding[String, OffsetDateTime](pgTimestampUtils.toOffsetDateTime)
  }
  implicit val offsetDatetimeEncoder: MappedEncoding[OffsetDateTime, String] = {
    MappedEncoding[OffsetDateTime, String](pgTimestampUtils.toString)
  }
}

/**
  * low-level bindings
  */
trait NOT_USED_QuillDateTimeEncoding extends LazyLogging {
  private val pgTimestampUtils = new TimestampUtils(true, () => TimeZone.getTimeZone("UTC"))
  protected val _quillContext: QuillCtxFactory.OurCtx
  import _quillContext.{encoder, Encoder, Decoder, decoder}

  implicit val offsetDatetimeEncoder: Encoder[OffsetDateTime] =
    encoder[OffsetDateTime](
      Types.TIMESTAMP,
      (index: Int, value: OffsetDateTime, row: PreparedStatement) => {
        row.setObject(index, pgTimestampUtils.toString(value), Types.TIMESTAMP)
      },
    )

  implicit val optionOffsetDateTimeEncoder: MappedEncoding[Option[OffsetDateTime], String] =
    MappedEncoding({
      case f @ Some(value) =>
        logger.debug("encoding OffsetDateTime: {} => {}", f, value)
        //        offsetDatetimeEncoder.f(value)
        "WTF"
      case f @ _ =>
        logger.debug("encoding OffsetDateTime: {} => {}", f, "NULL")
        "NULL"
        "2021-11-12"
    })
}
