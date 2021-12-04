package io.jokester.http_api

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.Instant
import scala.util.{Success, Try}

object OpenAPIAuthConvention {
  import OpenAPIConvention._
  case class BearerToken(value: String)
  case class AccessTokenPayload(userId: Int, tokenType: String)
  case class RefreshTokenPayload(userId: Int, tokenType: String)

  trait JwtHelper {

    def jwtSecret: String

    def signAccessToken(userId: Int): String =
      signToken(AccessTokenPayload(userId = userId, tokenType = "accessToken"), expireIn = 600)
    def signRefreshToken(userId: Int): String =
      signToken(
        RefreshTokenPayload(userId = userId, tokenType = "refreshToken"),
        expireIn = 3600 * 24 * 7,
      )

    def validateAccessToken(token: String): ApiResultSync[AccessTokenPayload] =
      decodeValidPayload[AccessTokenPayload](token) match {
        case Success(value) if value.tokenType == "accessToken" => Right(value)
        case _                                                  => Left(Unauthorized("invalid jwt token"))
      }

    def validateRefreshToken(token: String): ApiResultSync[RefreshTokenPayload] =
      decodeValidPayload[RefreshTokenPayload](token) match {
        case Success(value) if value.tokenType == "refreshToken" => Right(value)
        case _                                                   => Left(Unauthorized("invalid jwt token"))
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
