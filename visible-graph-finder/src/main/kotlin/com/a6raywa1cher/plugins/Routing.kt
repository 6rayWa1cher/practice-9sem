package com.a6raywa1cher.plugins

import com.a6raywa1cher.graph.graphRouting
import io.ktor.server.application.*

fun Application.configureRouting() {
    graphRouting()
}
