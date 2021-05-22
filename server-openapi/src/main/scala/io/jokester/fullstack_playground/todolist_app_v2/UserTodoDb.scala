package io.jokester.fullstack_playground.todolist_app_v2

import scalikejdbc.{DB}

trait UserTodoDb {
  protected def db: DB.type = DB
}
