package com.a6raywa1cher

import com.a6raywa1cher.plugins.configureHTTP
import com.a6raywa1cher.plugins.configureKoin
import com.a6raywa1cher.plugins.configureRouting
import com.a6raywa1cher.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureKoin()
    configureHTTP()
    configureSerialization()
    configureRouting()
}
