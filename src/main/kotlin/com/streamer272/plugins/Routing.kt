package com.streamer272.plugins

import com.streamer272.module.User
import com.streamer272.module.UserDTO
import com.streamer272.module.UserRegisterDTO
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.MessageDigest

fun Application.configureRouting() {
    routing {
        get("/") {
            val users: MutableList<UserDTO> = mutableListOf()
            transaction {
                User.all().forEach {
                    users.add(it.toDTO())
                }
            }

            call.respond(HttpStatusCode.OK, Json.encodeToString(users))
        }

        post("/register") {
            val user = call.receive<UserRegisterDTO>()
            var newUser: User? = null

            try {
                transaction {
                    newUser = User.new {
                        username = user.username
                        password = hashPassword(user.password)
                    }
                }
            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.BadRequest, "Failed to create user")
            }

            call.respond(HttpStatusCode.OK, Json.encodeToString(newUser?.toDTO()))
        }
    }
}

fun hashPassword(data: String): String {
    val bytes = data.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}
