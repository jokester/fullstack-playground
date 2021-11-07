package io.jokester.fullstack_playground.stated.hdbCatalog

case class HdbActionLog(actionName: Option[String], createdAt: java.time.LocalDateTime, responseReceivedAt: Option[java.time.LocalDateTime], status: String)

