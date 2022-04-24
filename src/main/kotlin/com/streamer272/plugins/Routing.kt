package com.streamer272.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.streamer272.entities.User
import com.streamer272.routes.setupAuth
import io.ktor.server.application.*
import java.security.MessageDigest

fun Application.configureRouting() {
    setupAuth()
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
