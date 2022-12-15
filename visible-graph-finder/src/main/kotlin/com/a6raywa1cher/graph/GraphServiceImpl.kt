package com.a6raywa1cher.graph

import org.slf4j.LoggerFactory
import java.lang.Math.floorMod
import kotlin.math.acos
import kotlin.math.sqrt

private data class Vector(val x: Int, val y: Int) {
    constructor(from: Point, to: Point) : this(to.x - from.x, to.y - from.y)

    fun length(): Double = sqrt((x * x + y * y).toDouble())

    operator fun times(other: Vector): Int = this.x * other.y - this.y * other.x

    infix fun dot(other: Vector): Int = this.x * other.x + this.y * other.y
}

class GraphServiceImpl : GraphService {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun vectorMultiplication(a: Point, b: Point, origin: Point): Int = Vector(origin, a) * Vector(origin, b)

    private fun intersects(p11: Point, p12: Point, p21: Point, p22: Point): Boolean {
        val t1 = vectorMultiplication(p22, p12, p11)
        val t2 = vectorMultiplication(p21, p12, p11)
        val d1 = vectorMultiplication(p22, p12, p21)
        val d2 = vectorMultiplication(p22, p11, p21)
        return t1 * t2 <= 0 && d1 * d2 <= 0
    }

    private fun degreesBetween(pl: Point, p: Point, pr: Point): Double {
        val v1 = Vector(p, pl)
        val v2 = Vector(p, pr)
        return acos((v1 dot v2) / (v1.length() * v2.length()))
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

        for (polygon1 in areaMap.polygons) {
            val polygon1Points = polygon1.points

            for ((i, p1) in polygon1Points.withIndex()) {
                val pl = polygon1Points[floorMod(i - 1, polygon1Points.size)]
                val pr = polygon1Points[floorMod(i + 1, polygon1Points.size)]

                for (polygon2 in areaMap.polygons) {
                    val polygon2Points = if (polygon1 == polygon2) polygon2.points else polygon2.getConvexPoints()

                    for (p2 in polygon2Points) {
                        if (p2.isInsideObservationArea(pl, p1, pr)) continue

                        if (p1 != p2) yield(Pair(p1, p2))
                    }
                }
            }
        }
    }

    override fun convertToVisibilityGraph(areaMap: AreaMap): VisibilityGraph {
        val visibleNeighbours = areaMap.polygons.flatMap { it.points }.associateWith { mutableSetOf<Point>() }
        outerLoop@ for ((p1, p2) in areaMap.linesToCheckSequence()) {
            for ((p3, p4) in areaMap.polygons.asSequence().flatMap { it.linesSequence() }) {
                if (setOf(p1, p2, p3, p4).size != 4) continue

                if (intersects(p1, p2, p3, p4)) {
                    continue@outerLoop
                }
            }
            logger.trace("$p1,$p2 +")
            visibleNeighbours[p1]!!.add(p2)
            visibleNeighbours[p2]!!.add(p1)
        }
        return visibleNeighbours.toVisibilityGraph()
    }
}