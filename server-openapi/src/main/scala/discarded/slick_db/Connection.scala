package discarded.slick_db
import com.typesafe.scalalogging.LazyLogging
import slick.jdbc.JdbcBackend.Database

object Connection {

  def connectPg(): Database = Database.forConfig("try-slick.pg")

  private def connectPg$(): Database = {
    val pgCred = "jdbc:postgresql://127.0.0.1:61432/try_hasura?user=pguser&password=secret"
    Class.forName("org.postgresql.Driver")
    Database.forURL(
      url = pgCred,
    )
  }
}

object SlickUseCases extends LazyLogging {}
