package com.a6raywa1cher.graph

import org.slf4j.LoggerFactory
import java.lang.Math.floorMod

class GraphServiceImpl : GraphService {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun vectorMultiplication(a: Point, b: Point, origin: Point): Int {
        val a1 = a.x - origin.x
        val a2 = a.y - origin.y
        val b1 = b.x - origin.x
        val b2 = b.y - origin.y
        return a1 * b2 - a2 * b1
    }

    private fun intersects(p11: Point, p12: Point, p21: Point, p22: Point): Boolean {
        val t1 = vectorMultiplication(p22, p12, p11)
        val t2 = vectorMultiplication(p21, p12, p11)
        val d1 = vectorMultiplication(p22, p12, p21)
        val d2 = vectorMultiplication(p22, p11, p21)
        return t1 * t2 <= 0 && d1 * d2 <= 0
    }

    private fun Polygon.getConvexPoints(): List<Point> {
        return this.points // TODO
    }

    private fun Point.isInsideObservationArea(pl: Point, p: Point, pr: Point): Boolean {
        return false // TODO
    }

    private fun Polygon.linesSequence() = sequence {
        if (points.size == 1) return@sequence
        for (i in points.indices) {
            val p1 = points[i]
            val p2 = points[floorMod(i + 1, points.size)]
            yield(Pair(p1, p2))
        }
    }

    private fun AreaMap.linesToCheckSequence() = sequence {
        val areaMap = this@linesToCheckSequence
//        val visitedPolygonPairs = areaMap.polygons.associateWith { mutableSetOf<Polygon>() }

        for (polygon1 in areaMap.polygons) {
            val polygon1Points = polygon1.points

            for ((i, p1) in polygon1Points.withIndex()) {
                val pl = polygon1Points[floorMod(i - 1, polygon1Points.size)]
                val pr = polygon1Points[floorMod(i + 1, polygon1Points.size)]

                for (polygon2 in areaMap.polygons) {
//                    if (visitedPolygonPairs[polygon1]!!.contains(polygon2)) continue

                    val polygon2Points = if (polygon1 == polygon2) polygon2.points else polygon2.getConvexPoints()

                    for (p2 in polygon2Points) {
                        if (p2.isInsideObservationArea(pl, p1, pr)) continue

                        if (p1 != p2) yield(Pair(p1, p2))
                    }

//                    visitedPolygonPairs[polygon1]!!.add(polygon2)
//                    visitedPolygonPairs[polygon2]!!.add(polygon1)
                }
            }
        }
    }

    override fun convertToVisibilityGraph(areaMap: AreaMap): VisibilityGraph {
        val visibleNeighbours = areaMap.polygons.flatMap { it.points }.associateWith { mutableSetOf<Point>() }
        outerLoop@ for ((p1, p2) in areaMap.linesToCheckSequence()) {
            logger.trace("$p1,$p2 ?")
            for ((p3, p4) in areaMap.polygons.asSequence().flatMap { it.linesSequence() }) {
                if (setOf(p1, p2, p3, p4).size != 4) continue
                logger.trace(">$p3,$p4 ?")
                if (intersects(p1, p2, p3, p4)) {
                    logger.trace(">$p3,$p4 -")
                    continue@outerLoop
                } else {
                    logger.trace(">$p3,$p4 +")
                }
            }
            logger.trace("$p1,$p2 +")
            visibleNeighbours[p1]!!.add(p2)
            visibleNeighbours[p2]!!.add(p1)
        }
        return VisibilityGraph(visibleNeighbours)
    }
}