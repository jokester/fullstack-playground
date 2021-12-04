package io.jokester.fullstack_playground.user_todolist_api

import cats.Id
import com.typesafe.scalalogging.LazyLogging
import scalikejdbc.scalikejdbcSQLInterpolationImplicitDef

import scala.concurrent.Future

abstract class UserTodoServiceImpl extends UserTodoService with LazyLogging {}
