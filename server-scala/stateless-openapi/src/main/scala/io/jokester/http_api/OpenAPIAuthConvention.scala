package io.jokester.http_api

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.Instant
import scala.util.{Success, Try}

object OpenAPIAuthConvention {
  import OpenAPIConvention._

  /**
    * token from request
    */
  case class TaintedToken(value: String) extends AnyVal

  /**
    * validated userId
    */
  case class UserId(value: Int)

  /**
    * payload to encode into JWT
    */
  case class AccessTokenPayload(userId: Int, tokenType: String)
  case class RefreshTokenPayload(userId: Int, tokenType: String)

  trait JwtHelper {
    self: Lifters =>

    def jwtSecret: String

    def signAccessToken(userId: Int): String =
      signToken(AccessTokenPayload(userId = userId, tokenType = "accessToken"), expireIn = 600)
    def signRefreshToken(userId: Int): String =
      signToken(
        RefreshTokenPayload(userId = userId, tokenType = "refreshToken"),
        expireIn = 3600 * 24 * 7,
      )

    def validateAccessToken(
        token: TaintedToken,
        expectedUserId: Int,
    ): Failable[UserId] =
      decodeValidPayload[AccessTokenPayload](token.value) match {
        case Success(value) if value.tokenType == "accessToken" && value.userId == expectedUserId =>
          liftSuccess(UserId(value.userId))
        case Success(value) => liftError(Unauthorized("accessToken unmatched"))
        case _              => liftError(Unauthorized("invalid jwt token"))
      }

    def validateRefreshToken(
        token: TaintedToken,
    ): Failable[UserId] =
      decodeValidPayload[RefreshTokenPayload](token.value) match {
        case Success(value) if (value.tokenType == "refreshToken") =>
          liftSuccess(UserId(value.userId))
        case _ => liftError(Unauthorized("invalid jwt token"))
      }

    private def now: Long = Instant.now().getEpochSecond

    private def signToken[T: Encoder](
        payload: T,
        expireIn: Int,
        issueAt: Long = now,
    ): String = {
      val claim = JwtClaim(
        content = payload.asJson.noSpaces,
        issuedAt = Some(issueAt),
        expiration = Some(issueAt + expireIn),
        notBefore = Some(issueAt),
      )

      JwtCirce.encode(claim, jwtSecret, JwtAlgorithm.HS256)
    }

    private def decodeValidPayload[T: Decoder](token: String): Try[T] = {
      val decoder = implicitly[Decoder[T]]
      val decoded = {
        for (
          claim <- JwtCirce.decode(token, jwtSecret, Seq(JwtAlgorithm.HS256)) if (
            claim.expiration.exists(_ > now)
              && claim.notBefore.exists(_ <= now)
          );
          payload        <- parser.parse(claim.content).toTry;
          decodedPayload <- decoder.decodeJson(payload).toTry
        ) yield decodedPayload
      }
      decoded
    }

  }
}
