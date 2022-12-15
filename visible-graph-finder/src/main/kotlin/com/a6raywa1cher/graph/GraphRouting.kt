package com.a6raywa1cher.graph

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.graphRouting() = routing {
    val graphService: GraphService by inject()

    post("/visibility") {
        val req = call.receive<Array<Array<Point>>>()
        val areaMap = AreaMap(polygons = req.map { Polygon(it.toList()) })
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)
        call.respond(visibilityGraph)
    }
}