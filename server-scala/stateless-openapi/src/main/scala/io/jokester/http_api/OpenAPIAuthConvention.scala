package io.jokester.http_api

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import cats.syntax.either._
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.Instant
import scala.concurrent.Future
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
  final case class BearerTokenPayload(userId: Int, tokenType: String) {
    def assertTokenType(expected: String): Failable[BearerTokenPayload] = {
      if (tokenType == expected) this.asRight
      else OpenAPIConvention.BadRequest("invalid token type").asLeft
    }
    def assertUserId(expectedUserId: Int): Failable[UserId] =
      if (userId == expectedUserId) UserId(userId).asRight
      else OpenAPIConvention.BadRequest("invalid userId").asLeft
  }
  case class RefreshTokenPayload(userId: Int, tokenType: String)

  trait JwtHelper {

    def decodeAccessToken(t: TaintedToken): Failable[BearerTokenPayload] = {
      decodeBearerToken(t)
        .filterOrElse(_.tokenType == "accessToken", BadRequest("invalid token type"))
    }

    def decodeBearerToken(t: TaintedToken): Failable[BearerTokenPayload] =
      decodeValidPayload[BearerTokenPayload](t.value) match {
        case Success(value)
            if value.tokenType == "accessToken" || value.tokenType == "refreshToken" =>
          value.asRight
        case _ => Unauthorized("Invalid jwt token").asLeft
      }

    def jwtSecret: String

    def signAccessToken(userId: Int): String =
      signToken(BearerTokenPayload(userId, tokenType = "accessToken"), expireIn = 600)
    def signRefreshToken(userId: Int): String =
      signToken(
        BearerTokenPayload(userId = userId, tokenType = "refreshToken"),
        expireIn = 3600 * 24 * 7,
      )

    def validateAccessToken(
        tokenPayload: BearerTokenPayload,
        expectedUserId: Int,
        expectedTokenType: String = "accessToken",
    ): Failable[UserId] = {
      tokenPayload
        .assertTokenType(expectedTokenType)
        .flatMap(_.assertUserId(expectedUserId))
    }

    /**
      * @deprecated
      */
    def validateAccessToken(
        token: TaintedToken,
        expectedUserId: Int,
    ): Failable[UserId] = {
      for (
        t <- decodeBearerToken(token);
        u <- validateAccessToken(t, expectedUserId)
      ) yield u
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
