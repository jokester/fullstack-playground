package io.jokester.fullstack_playground.quill.generated.hdbCatalog

case class HdbSchemaNotifications(id: Int, notification: String, resourceVersion: Int, instanceId: java.util.UUID, updatedAt: Option[java.time.OffsetDateTime])

case class HdbVersion(hasuraUuid: java.util.UUID, version: String, upgradedOn: java.time.OffsetDateTime, cliState: String, consoleState: String)

case class HdbScheduledEventInvocationLogs(id: String, eventId: Option[String], status: Option[Int], request: Option[String], response: Option[String], createdAt: Option[java.time.OffsetDateTime])

case class HdbCronEventInvocationLogs(id: String, eventId: Option[String], status: Option[Int], request: Option[String], response: Option[String], createdAt: Option[java.time.OffsetDateTime])

case class HdbScheduledEvents(id: String, webhookConf: String, scheduledTime: java.time.OffsetDateTime, retryConf: Option[String], payload: Option[String], headerConf: Option[String], status: String, tries: Int, createdAt: Option[java.time.OffsetDateTime], nextRetryAt: Option[java.time.OffsetDateTime], comment: Option[String])

case class HdbCronEvents(id: String, triggerName: String, scheduledTime: java.time.OffsetDateTime, status: String, tries: Int, createdAt: Option[java.time.OffsetDateTime], nextRetryAt: Option[java.time.OffsetDateTime])

case class HdbActionLog(id: java.util.UUID, actionName: Option[String], inputPayload: String, requestHeaders: String, sessionVariables: String, responsePayload: Option[String], errors: Option[String], createdAt: java.time.OffsetDateTime, responseReceivedAt: Option[java.time.OffsetDateTime], status: String)

case class HdbMetadata(id: Int, metadata: String, resourceVersion: Int)

trait HdbCatalogExtensions[Idiom <: io.getquill.idiom.Idiom, Naming <: io.getquill.NamingStrategy] {
  this:io.getquill.context.Context[Idiom, Naming] =>

  object HdbCatalogSchema {
    object HdbSchemaNotificationsDao {
        def query = quote {
            querySchema[HdbSchemaNotifications](
              "hdb_catalog.hdb_schema_notifications",
              _.id -> "id",
              _.notification -> "notification",
              _.resourceVersion -> "resource_version",
              _.instanceId -> "instance_id",
              _.updatedAt -> "updated_at"
            )
                      
          }
                    
      }

      object HdbVersionDao {
        def query = quote {
            querySchema[HdbVersion](
              "hdb_catalog.hdb_version",
              _.hasuraUuid -> "hasura_uuid",
              _.version -> "version",
              _.upgradedOn -> "upgraded_on",
              _.cliState -> "cli_state",
              _.consoleState -> "console_state"
            )
                      
          }
                    
      }

      object HdbScheduledEventInvocationLogsDao {
        def query = quote {
            querySchema[HdbScheduledEventInvocationLogs](
              "hdb_catalog.hdb_scheduled_event_invocation_logs",
              _.id -> "id",
              _.eventId -> "event_id",
              _.status -> "status",
              _.request -> "request",
              _.response -> "response",
              _.createdAt -> "created_at"
            )
                      
          }
                    
      }

      object HdbCronEventInvocationLogsDao {
        def query = quote {
            querySchema[HdbCronEventInvocationLogs](
              "hdb_catalog.hdb_cron_event_invocation_logs",
              _.id -> "id",
              _.eventId -> "event_id",
              _.status -> "status",
              _.request -> "request",
              _.response -> "response",
              _.createdAt -> "created_at"
            )
                      
          }
                    
      }

      object HdbScheduledEventsDao {
        def query = quote {
            querySchema[HdbScheduledEvents](
              "hdb_catalog.hdb_scheduled_events",
              _.id -> "id",
              _.webhookConf -> "webhook_conf",
              _.scheduledTime -> "scheduled_time",
              _.retryConf -> "retry_conf",
              _.payload -> "payload",
              _.headerConf -> "header_conf",
              _.status -> "status",
              _.tries -> "tries",
              _.createdAt -> "created_at",
              _.nextRetryAt -> "next_retry_at",
              _.comment -> "comment"
            )
                      
          }
                    
      }

      object HdbCronEventsDao {
        def query = quote {
            querySchema[HdbCronEvents](
              "hdb_catalog.hdb_cron_events",
              _.id -> "id",
              _.triggerName -> "trigger_name",
              _.scheduledTime -> "scheduled_time",
              _.status -> "status",
              _.tries -> "tries",
              _.createdAt -> "created_at",
              _.nextRetryAt -> "next_retry_at"
            )
                      
          }
                    
      }

      object HdbActionLogDao {
        def query = quote {
            querySchema[HdbActionLog](
              "hdb_catalog.hdb_action_log",
              _.id -> "id",
              _.actionName -> "action_name",
              _.inputPayload -> "input_payload",
              _.requestHeaders -> "request_headers",
              _.sessionVariables -> "session_variables",
              _.responsePayload -> "response_payload",
              _.errors -> "errors",
              _.createdAt -> "created_at",
              _.responseReceivedAt -> "response_received_at",
              _.status -> "status"
            )
                      
          }
                    
      }

      object HdbMetadataDao {
        def query = quote {
            querySchema[HdbMetadata](
              "hdb_catalog.hdb_metadata",
              _.id -> "id",
              _.metadata -> "metadata",
              _.resourceVersion -> "resource_version"
            )
                      
          }
                    
      }
  }
}
