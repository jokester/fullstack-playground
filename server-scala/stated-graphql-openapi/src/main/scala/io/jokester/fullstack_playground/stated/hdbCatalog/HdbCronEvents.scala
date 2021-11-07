package io.jokester.fullstack_playground.stated.hdbCatalog

case class HdbCronEvents(id: String, triggerName: String, scheduledTime: java.time.LocalDateTime, status: String, tries: Int, createdAt: Option[java.time.LocalDateTime], nextRetryAt: Option[java.time.LocalDateTime])

