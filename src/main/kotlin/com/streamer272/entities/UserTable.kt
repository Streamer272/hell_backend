package com.streamer272.entities

import com.streamer272.dtos.UserDTO
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object UserTable : IntIdTable() {
    val username: Column<String> = varchar("username", 32).uniqueIndex()
    val password: Column<String> = varchar("password", 256)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(UserTable)
    var username by UserTable.username
    var password by UserTable.password

    fun toDTO(): UserDTO {
        return UserDTO(
            id.value,
            username,
        )
    }
}

