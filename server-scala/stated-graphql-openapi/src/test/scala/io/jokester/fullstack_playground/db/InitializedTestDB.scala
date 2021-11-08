package io.jokester.fullstack_playground.db

import io.jokester.fullstack_playground.scalikejdbc.DatabaseInit

trait InitializedTestDB {
  DatabaseInit.setupTest
}
