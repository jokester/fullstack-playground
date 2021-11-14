package io.jokester.fullstack_playground.quill

import com.typesafe.scalalogging.LazyLogging
import io.getquill.MappedEncoding
import org.postgresql.jdbc.TimestampUtils

import java.sql.{PreparedStatement, Types}
import java.time.OffsetDateTime
import java.util.TimeZone

trait QuillDatetimeEncoding2 extends LazyLogging {
  private val pgTimestampUtils = new TimestampUtils(true, () => TimeZone.getTimeZone("UTC"))
  protected val _quillContext: QuillCtxFactory.OurCtx
  implicit val offsetDatetimeDecoder: MappedEncoding[String, OffsetDateTime] = {
    MappedEncoding[String, OffsetDateTime](pgTimestampUtils.toOffsetDateTime)
  }
  implicit val offsetDatetimeEncoder: MappedEncoding[OffsetDateTime, Object] = {
    MappedEncoding[OffsetDateTime, Object](pgTimestampUtils.toString)
  }
}

/**
  * low-level bindings
  */
trait QuillDatetimeEncoding extends LazyLogging {
  private val pgTimestampUtils = new TimestampUtils(true, () => TimeZone.getTimeZone("UTC"))
  protected val _quillContext: QuillCtxFactory.OurCtx
  import _quillContext.{encoder, Encoder, Decoder, decoder}

  implicit lazy val offsetDatetimeDecoder: MappedEncoding[String, OffsetDateTime] = {
    MappedEncoding[String, OffsetDateTime](pgTimestampUtils.toOffsetDateTime)
  }

  implicit lazy val optionX: Encoder[Option[OffsetDateTime]] = encoder[Option[OffsetDateTime]](
    Types.TIMESTAMP_WITH_TIMEZONE,
    (index: Int, value: Option[OffsetDateTime], row: PreparedStatement) => {
      logger.debug(s"offsetDatetimeEncoder: index=$index, value=$value")
      //        row.setObject(index, pgTimestampUtils.toString(value), Types.TIMESTAMP)
    },
  )

  implicit lazy val offsetDatetimeEncoder: Encoder[OffsetDateTime] =
    encoder[OffsetDateTime](
      Types.TIMESTAMP_WITH_TIMEZONE,
      (index: Int, value: OffsetDateTime, row: PreparedStatement) => {
        logger.debug(s"offsetDatetimeEncoder: index=$index, value=$value")
        //        row.setObject(index, pgTimestampUtils.toString(value), Types.TIMESTAMP)
      },
    )
}
