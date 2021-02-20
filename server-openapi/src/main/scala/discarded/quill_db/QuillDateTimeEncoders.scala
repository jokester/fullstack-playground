package discarded.quill_db

import io.getquill.MappedEncoding

import java.time.{OffsetDateTime, ZonedDateTime}
import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}
import java.time.temporal.ChronoField

/**
  * taken from https://github.com/gustavoamigo/quill-pgsql
  */
trait QuillDateTimeEncoders {

  private lazy val bpTzDateTimeFormatter =
    new DateTimeFormatterBuilder()
      .append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      .optionalStart()
      .appendFraction(ChronoField.NANO_OF_SECOND, 0, 6, true)
      .optionalEnd()
      .appendOffset("+HH:mm", "+00")
      .toFormatter()

//  implicit val encodeZonedDateTime: MappedEncoding[OffsetDateTime, String] =
//    MappedEncoding(z => bpTzDateTimeFormatter.format(z))

  implicit val encodeZonedDateTime2: MappedEncoding[Option[OffsetDateTime], ZonedDateTime] =
//    MappedEncoding(_.map(bpTzDateTimeFormatter.format(_)))
    MappedEncoding(whatever => ZonedDateTime.now())

  implicit val decodeZonedDateTime: MappedEncoding[String, OffsetDateTime] =
    MappedEncoding(d => OffsetDateTime.parse(d, bpTzDateTimeFormatter))

}
