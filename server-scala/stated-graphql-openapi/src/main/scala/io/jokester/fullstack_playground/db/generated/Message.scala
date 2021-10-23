package io.jokester.fullstack_playground.db.generated

import scalikejdbc._

case class Message(messageId: Long, userId: Int, roomId: Int, content: String) {

  def save()(implicit session: DBSession): Message = Message.save(this)(session)

  def destroy()(implicit session: DBSession): Int = Message.destroy(this)(session)

}

object Message extends SQLSyntaxSupport[Message] {

  override val schemaName = Some("chatroom")

  override val tableName = "message"

  override val columns = Seq("message_id", "user_id", "room_id", "content")

  def apply(m: SyntaxProvider[Message])(rs: WrappedResultSet): Message = apply(m.resultName)(rs)
  def apply(m: ResultName[Message])(rs: WrappedResultSet): Message =
    new Message(
      messageId = rs.get(m.messageId),
      userId = rs.get(m.userId),
      roomId = rs.get(m.roomId),
      content = rs.get(m.content),
    )

  val m = Message.syntax("m")

  override val autoSession = AutoSession

  def find(messageId: Long)(implicit session: DBSession): Option[Message] = {
    sql"""select ${m.result.*} from ${Message as m} where ${m.messageId} = ${messageId}"""
      .map(Message(m.resultName))
      .single
      .apply()
  }

  def findAll()(implicit session: DBSession): List[Message] = {
    sql"""select ${m.result.*} from ${Message as m}""".map(Message(m.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    sql"""select count(1) from ${Message.table}""".map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[Message] = {
    sql"""select ${m.result.*} from ${Message as m} where ${where}"""
      .map(Message(m.resultName))
      .single
      .apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[Message] = {
    sql"""select ${m.result.*} from ${Message as m} where ${where}"""
      .map(Message(m.resultName))
      .list
      .apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    sql"""select count(1) from ${Message as m} where ${where}"""
      .map(_.long(1))
      .single
      .apply()
      .get
  }

  def create(userId: Int, roomId: Int, content: String)(implicit session: DBSession): Message = {
    val generatedKey = sql"""
      insert into ${Message.table} (
        ${column.userId},
        ${column.roomId},
        ${column.content}
      ) values (
        ${userId},
        ${roomId},
        ${content}
      )
      """.updateAndReturnGeneratedKey.apply()

    Message(messageId = generatedKey, userId = userId, roomId = roomId, content = content)
  }

  def batchInsert(entities: collection.Seq[Message])(implicit session: DBSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("userId")  -> entity.userId,
        Symbol("roomId")  -> entity.roomId,
        Symbol("content") -> entity.content,
      ),
    )
    SQL("""insert into message(
      user_id,
      room_id,
      content
    ) values (
      {userId},
      {roomId},
      {content}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Message)(implicit session: DBSession): Message = {
    sql"""
      update
        ${Message.table}
      set
        ${column.messageId} = ${entity.messageId},
        ${column.userId} = ${entity.userId},
        ${column.roomId} = ${entity.roomId},
        ${column.content} = ${entity.content}
      where
        ${column.messageId} = ${entity.messageId}
      """.update.apply()
    entity
  }

  def destroy(entity: Message)(implicit session: DBSession): Int = {
    sql"""delete from ${Message.table} where ${column.messageId} = ${entity.messageId}""".update
      .apply()
  }

}
