package io.jokester.fullstack_playground.scalikejdbc_db

import io.circe
import io.circe.{Decoder, Encoder, Json, JsonObject}
import io.jokester.fullstack_playground.genereated_scalikejdbc.UserProfile
import org.postgresql.util.PGobject
import scalikejdbc.Binders

trait GenericBinders {
  val jsonBinder: Binders[Json] = Binders.of[circe.Json] { fromDB => circe.Json.obj() } {
    fromJvm => (stmt, idx) =>
      val jsonObject = new PGobject()
      jsonObject.setType("json")
      jsonObject.setValue(fromJvm.noSpaces)
      stmt.setObject(idx, jsonObject)
  }

  val jsonObjBinder: Binders[JsonObject] =
    jsonBinder.xmap(fromDB => fromDB.asObject.get, Json.fromJsonObject)

  protected def customObject[O: Encoder: Decoder]: Binders[O] = {
    val encoder = implicitly[Encoder[O]]
    val decoder = implicitly[Decoder[O]]
    jsonBinder
      .xmap[O](fromDB => decoder.decodeJson(fromDB).toOption.get, fromJvm => encoder(fromJvm))
  }

}

trait OurBinders extends GenericBinders {
  import io.circe.generic.auto._

  implicit val userProfileBinder: Binders[UserProfile] = customObject
}
