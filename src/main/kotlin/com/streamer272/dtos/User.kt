package com.streamer272.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO (
    val id: Int,
    val username: String,
)

@Serializable
data class UserAuthDTO (
    val username: String,
    val password: String,
)
