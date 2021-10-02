package io.jokester.fullstack_playground.db.generated

import scalikejdbc._
import java.time.{OffsetDateTime}

case class Room(roomId: Int, name: String, createdAt: OffsetDateTime) {

  def save()(implicit session: DBSession): Room = Room.save(this)(session)

  def destroy()(implicit session: DBSession): Int = Room.destroy(this)(session)

}

object Room extends SQLSyntaxSupport[Room] {

  override val schemaName = Some("chatroom")

  override val tableName = "room"

  override val columns = Seq("room_id", "name", "created_at")

  def apply(r: SyntaxProvider[Room])(rs: WrappedResultSet): Room = apply(r.resultName)(rs)
  def apply(r: ResultName[Room])(rs: WrappedResultSet): Room =
    new Room(
      roomId = rs.get(r.roomId),
      name = rs.get(r.name),
      createdAt = rs.get(r.createdAt),
    )

  val r = Room.syntax("r")

  override val autoSession = AutoSession

  def find(roomId: Int)(implicit session: DBSession): Option[Room] = {
    sql"""select ${r.result.*} from ${Room as r} where ${r.roomId} = ${roomId}"""
      .map(Room(r.resultName))
      .single
      .apply()
  }

  def findAll()(implicit session: DBSession): List[Room] = {
    sql"""select ${r.result.*} from ${Room as r}""".map(Room(r.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    sql"""select count(1) from ${Room.table}""".map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[Room] = {
    sql"""select ${r.result.*} from ${Room as r} where ${where}"""
      .map(Room(r.resultName))
      .single
      .apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[Room] = {
    sql"""select ${r.result.*} from ${Room as r} where ${where}"""
      .map(Room(r.resultName))
      .list
      .apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    sql"""select count(1) from ${Room as r} where ${where}"""
      .map(_.long(1))
      .single
      .apply()
      .get
  }

  def create(name: String, createdAt: OffsetDateTime)(implicit session: DBSession): Room = {
    val generatedKey = sql"""
      insert into ${Room.table} (
        ${column.name},
        ${column.createdAt}
      ) values (
        ${name},
        ${createdAt}
      )
      """.updateAndReturnGeneratedKey.apply()

    Room(roomId = generatedKey.toInt, name = name, createdAt = createdAt)
  }

  def batchInsert(entities: collection.Seq[Room])(implicit session: DBSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(Symbol("name") -> entity.name, Symbol("createdAt") -> entity.createdAt),
    )
    SQL("""insert into room(
      name,
      created_at
    ) values (
      {name},
      {createdAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Room)(implicit session: DBSession): Room = {
    sql"""
      update
        ${Room.table}
      set
        ${column.roomId} = ${entity.roomId},
        ${column.name} = ${entity.name},
        ${column.createdAt} = ${entity.createdAt}
      where
        ${column.roomId} = ${entity.roomId}
      """.update.apply()
    entity
  }

  def destroy(entity: Room)(implicit session: DBSession): Int = {
    sql"""delete from ${Room.table} where ${column.roomId} = ${entity.roomId}""".update.apply()
  }

}
