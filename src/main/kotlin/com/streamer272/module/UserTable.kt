package com.streamer272.module

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object UserTable : IntIdTable() {
    val username: Column<String> = varchar("username", 32).uniqueIndex()
    val password: Column<String> = varchar("password", 256)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(UserTable)
    var username by UserTable.username
    var password by UserTable.password

    override fun toString(): String {
        return Json.encodeToString(UserDTO(id.value, username))
    }
}

@Serializable
data class UserDTO (
    val id: Int,
    val username: String,
)
