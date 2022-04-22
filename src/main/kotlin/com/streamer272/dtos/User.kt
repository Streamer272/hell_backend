package com.streamer272.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO (
    val id: Int,
    val username: String,
)

@Serializable
data class UserLoginDTO (
    val username: String,
    val password: String,
)

@Serializable
data class UserRegisterDTO (
    val username: String,
    val password: String
)
