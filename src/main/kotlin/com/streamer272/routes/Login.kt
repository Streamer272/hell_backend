package com.streamer272.routes

import com.streamer272.dtos.ErrorDTO
import com.streamer272.dtos.TokenDTO
import com.streamer272.dtos.UserAuthDTO
import com.streamer272.entities.User
import com.streamer272.entities.UserTable
import com.streamer272.plugins.generateToken
import com.streamer272.plugins.hash
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.setupAuth() {
    routing {
        post("/auth/login") {
            val login = call.receive<UserAuthDTO>()

            val user = transaction {
                User.find { UserTable.username eq login.username and (UserTable.password eq hash(login.password)) }.firstOrNull()
            } ?: return@post call.respond(HttpStatusCode.Unauthorized, ErrorDTO("Invalid username or password"))

            val token = TokenDTO(generateToken(user))
            call.respond(HttpStatusCode.OK, token)
        }

        post("/auth/register") {
            val user = call.receive<UserAuthDTO>()

            try {
                val newUser = transaction {
                    User.new {
                        username = user.username
                        password = hash(user.password)
                    }
                }

                val token = TokenDTO(generateToken(newUser))
                call.respond(HttpStatusCode.OK, token)
            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.BadRequest, ErrorDTO("Failed to create user", e.message))
            }
        }
    }
}
