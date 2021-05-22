package io.jokester.fullstack_playground.utils

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, _}
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.Instant
import scala.util.Try

object JwtHelpers {
  protected case class AccessTokenPayload(userId: Int, tokenType: String)
  protected case class RefreshTokenPayload(userId: Int, tokenType: String)
}

trait JwtHelpers {
  import JwtHelpers._

  // TODO: change to configuration
  private lazy val secret = "secret-key"

  def signAccessToken(userId: Int): String =
    signToken(AccessTokenPayload(userId = userId, tokenType = "accessToken"), Some(3600))
  def signRefreshToken(userId: Int): String =
    signToken(RefreshTokenPayload(userId = userId, tokenType = "refreshToken"), Some(3600 * 24 * 7))

  def validateAccessToken(token: String): Option[AccessTokenPayload] =
    validate[AccessTokenPayload](token).filter(_.tokenType == "accessToken").toOption
  def validateRefreshToken(token: String): Option[RefreshTokenPayload] =
    validate[RefreshTokenPayload](token).filter(_.tokenType == "refreshToken").toOption

  private def signToken[T: Encoder](
      payload: T,
      expireIn: Option[Long] = None,
      issueAt: Long = Instant.now().getEpochSecond,
  ): String = {
    val claim = JwtClaim(
      content = payload.asJson.noSpaces,
      issuedAt = Some(issueAt),
      expiration = Some(issueAt + expireIn.getOrElse(3600L)),
      notBefore = Some(issueAt),
    )

    JwtCirce.encode(claim, secret, JwtAlgorithm.HS256)
  }

  private def validate[T: Decoder](token: String): Try[T] = {
    val decoder = implicitly[Decoder[T]]
    val decoded = {
      for (
        claim <- JwtCirce.decode(token, secret, Seq(JwtAlgorithm.HS256)) if (
          claim.expiration.getOrElse(Long.MinValue) >= Instant.now().getEpochSecond
            && claim.notBefore.getOrElse(Long.MaxValue) <= Instant.now().getEpochSecond
        );
        payload        <- parser.parse(claim.content).toTry;
        decodedPayload <- decoder.decodeJson(payload).toTry
      ) yield decodedPayload
    }
    decoded
  }
}
