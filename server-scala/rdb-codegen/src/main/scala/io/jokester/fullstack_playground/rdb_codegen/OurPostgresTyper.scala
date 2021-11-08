package io.jokester.fullstack_playground.rdb_codegen

import com.typesafe.scalalogging.LazyLogging

import java.sql.{Types => JDBCTypes}
import io.getquill.codegen.jdbc.model.{DefaultJdbcTyper, JdbcTypeInfo}
import io.getquill.codegen.model.{NumericPreference, UnrecognizedTypeStrategy}

import java.time.OffsetDateTime
import java.util.UUID
import scala.reflect.{ClassTag, classTag}

class OurPostgresTyper(
    strategy: UnrecognizedTypeStrategy,
    numericPreference: NumericPreference,
) extends DefaultJdbcTyper(strategy, numericPreference)
    with LazyLogging {
  override def apply(jdbcTypeInfo: JdbcTypeInfo): Option[ClassTag[_]] = {
    logger.debug("jdbcTypeInfo: {}", jdbcTypeInfo)
    val typed = jdbcTypeInfo match {
      case JdbcTypeInfo(jdbcType, size, Some("timestamptz")) if jdbcType == JDBCTypes.TIMESTAMP =>
        Some(classTag[OffsetDateTime])
      case JdbcTypeInfo(jdbcType, size, Some("json")) if jdbcType == JDBCTypes.OTHER =>
        Some(classTag[String])
      case JdbcTypeInfo(jdbcType, size, Some("jsonb")) if jdbcType == JDBCTypes.OTHER =>
        Some(classTag[String])
      case JdbcTypeInfo(jdbcType, size, Some("uuid")) if jdbcType == JDBCTypes.OTHER =>
        Some(classTag[UUID])
      case _ => super.apply(jdbcTypeInfo)
    }
    logger.debug("mapping {} to {}", jdbcTypeInfo, typed)
    typed
  }
}
