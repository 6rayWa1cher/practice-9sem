package com.a6raywa1cher.plugins;

import com.a6raywa1cher.graph.graphModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(graphModule)
    }
}