package com.a6raywa1cher.plugins

import com.a6raywa1cher.graph.AreaMap
import com.a6raywa1cher.graph.GraphService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val graphService: GraphService by inject()
    routing {
        get("/") {
            val areaMap = AreaMap(listOf())
            val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)
            call.respondText("Hello World!")
        }
    }
}
