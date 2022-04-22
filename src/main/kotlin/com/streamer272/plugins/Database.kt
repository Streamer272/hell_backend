package com.streamer272.plugins

import com.streamer272.module.UserTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    Database.connect("jdbc:postgresql://localhost:5432/hell", "org.postgresql.Driver", "user", "password")
    transaction {
        SchemaUtils.create(UserTable)
    }
}
