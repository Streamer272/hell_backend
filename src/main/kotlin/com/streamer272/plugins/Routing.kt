package com.streamer272.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.streamer272.dtos.*
import com.streamer272.entities.User
import com.streamer272.entities.UserTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.MessageDigest

fun Application.configureRouting() {
    routing {
        post("/login") {
            val login = call.receive<UserLoginDTO>()

            val user = transaction {
                User.find { UserTable.username eq login.username and (UserTable.password eq hash(login.password)) }.firstOrNull()
            } ?: return@post call.respond(HttpStatusCode.Unauthorized, ErrorDTO("Invalid username or password"))

            val token = TokenDTO(generateToken(user))
            call.respond(HttpStatusCode.OK, Json.encodeToString(token))
        }

        post("/register") {
            val user = call.receive<UserRegisterDTO>()

            try {
                val newUser = transaction {
                    User.new {
                        username = user.username
                        password = hash(user.password)
                    }
                }

                call.respond(HttpStatusCode.OK, newUser.toDTO())
            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.BadRequest, ErrorDTO("Failed to create user", e.message))
            }
        }
    }
}

// sha256 hash
fun hash(data: String): String {
    val bytes = data.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}

fun generateToken(user: User): String {
    return JWT.create()
        .withClaim("id", user.id.value)
        .withClaim("username", user.username)
        .withClaim("password", user.password)
        .sign(Algorithm.HMAC256(JWT_SECRET))
}
