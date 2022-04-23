package com.streamer272.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO (
    val message: String,
    val description: String? = null
)
