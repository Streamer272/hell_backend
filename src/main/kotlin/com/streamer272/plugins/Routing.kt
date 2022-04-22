package com.streamer272.plugins

import com.streamer272.module.UserTable
import com.streamer272.module.User
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {
    routing {
        get("/") {
            val users: MutableList<UserTable> = mutableListOf()
            transaction {
                User.all().forEach {
                    println("It is $it")
                }
            }

            call.respond(HttpStatusCode.OK, "xd")
        }
    }
}
