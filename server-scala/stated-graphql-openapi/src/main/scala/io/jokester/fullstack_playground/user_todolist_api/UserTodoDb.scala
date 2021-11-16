package io.jokester.fullstack_playground.user_todolist_api

import scalikejdbc.{DB}

trait UserTodoDb {
  protected def db: DB.type = DB
}
