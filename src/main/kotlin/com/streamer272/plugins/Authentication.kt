package com.streamer272.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

const val JWT_SECRET = "lmao bad"

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt {
            realm = "ktor.io"
            verifier(JWT
                .require(Algorithm.HMAC512(JWT_SECRET))
                .withAudience("hell.streamer272.com")
                .withIssuer("hell.streamer272.com")
                .build())
            validate {
                if (it.payload.getClaim("username").asString() == "") {
                    null
                }
                else {
                    JWTPrincipal(it.payload)
                }
            }
            challenge {_, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token invalid")
            }
        }
    }
}
