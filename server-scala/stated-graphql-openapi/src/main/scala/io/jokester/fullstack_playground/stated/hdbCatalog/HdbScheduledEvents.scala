package io.jokester.fullstack_playground.stated.hdbCatalog

case class HdbScheduledEvents(id: String, scheduledTime: java.time.LocalDateTime, status: String, tries: Int, createdAt: Option[java.time.LocalDateTime], nextRetryAt: Option[java.time.LocalDateTime], comment: Option[String])

