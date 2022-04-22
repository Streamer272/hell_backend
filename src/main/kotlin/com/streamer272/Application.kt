package com.streamer272

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.streamer272.plugins.*

fun main() {
    embeddedServer(Netty, port = 8000, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureMonitoring()
    }.start(wait = true)
}
