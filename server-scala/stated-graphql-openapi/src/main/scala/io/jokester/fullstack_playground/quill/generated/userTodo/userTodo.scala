package io.jokester.fullstack_playground.quill.generated.userTodo

case class UserTodos(todoId: Long, userId: Long, title: String, description: String, finishedAt: java.time.OffsetDateTime, createdAt: java.time.OffsetDateTime, updatedAt: java.time.OffsetDateTime)

case class Users(userId: Long, email: String, passwordHash: String, createdAt: java.time.OffsetDateTime, updatedAt: java.time.OffsetDateTime, profile: String)

trait UserTodoExtensions[Idiom <: io.getquill.idiom.Idiom, Naming <: io.getquill.NamingStrategy] {
  this:io.getquill.context.Context[Idiom, Naming] =>

  object UserTodoSchema {
    object UserTodosDao {
        def query = quote {
            querySchema[UserTodos](
              "user_todo.user_todos",
              _.todoId -> "todo_id",
              _.userId -> "user_id",
              _.title -> "title",
              _.description -> "description",
              _.finishedAt -> "finished_at",
              _.createdAt -> "created_at",
              _.updatedAt -> "updated_at"
            )
                      
          }
                    
      }

      object UsersDao {
        def query = quote {
            querySchema[Users](
              "user_todo.users",
              _.userId -> "user_id",
              _.email -> "email",
              _.passwordHash -> "password_hash",
              _.createdAt -> "created_at",
              _.updatedAt -> "updated_at",
              _.profile -> "profile"
            )
                      
          }
                    
      }
  }
}
