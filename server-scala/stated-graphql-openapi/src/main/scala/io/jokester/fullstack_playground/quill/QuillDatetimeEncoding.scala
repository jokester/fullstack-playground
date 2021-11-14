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

  protected val ctx: QuillCtxFactory.OurCtx

  /**
    * implicit values must be of path-dependent type, not "OurCtx#decoder"
    */
  protected lazy implicit val offsetDateTimeDecoder: ctx.type#Decoder[OffsetDateTime] =
    ctx.decoder((index, row, session) => row.getObject(index, classOf[OffsetDateTime]))
  protected lazy implicit val offsetDateTimeEncoder: ctx.type#Encoder[OffsetDateTime] =
    ctx.encoder(
      Types.TIMESTAMP_WITH_TIMEZONE,
      (index, value, row) => {
        row.setObject(index, value)
      },
    )

}
