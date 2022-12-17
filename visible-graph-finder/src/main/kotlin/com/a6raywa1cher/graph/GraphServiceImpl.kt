package com.a6raywa1cher.graph

import org.slf4j.LoggerFactory
import java.lang.Math.floorMod
import kotlin.math.sqrt

internal data class Vector(val x: Int, val y: Int) {
    constructor(from: Point, to: Point) : this(to.x - from.x, to.y - from.y)

    fun length(): Double = sqrt((x * x + y * y).toDouble())

    operator fun times(other: Vector): Int = this.x * other.y - this.y * other.x

    infix fun dot(other: Vector): Int = this.x * other.x + this.y * other.y
}

internal fun Point.isInsideObservationArea(pl: Point, p: Point, pr: Point): Boolean {
    val v1 = Vector(p, pl)
    val v2 = Vector(p, pr)
    val v3 = Vector(p, this)
    if (v1 * v2 == 0) return false
    return if (v1 * v2 < 0) v1 * v3 < 0 && v3 * v2 < 0 else v1 * v3 > 0 && v3 * v2 > 0
}

internal fun getPrevIndex(i: Int, list: List<*>) = floorMod(i - 1, list.size)

internal fun getNextIndex(i: Int, list: List<*>) = floorMod(i + 1, list.size)

internal fun Polygon.getConvexPoints(): List<Point> {
    if (points.size < 3) return points
    val out = mutableListOf<Point>()
    for (i in points.indices) {
        val pl = points[getPrevIndex(i, points)]
        val p = points[i]
        val pr = points[getNextIndex(i, points)]
        if (Vector(pl, pr) * Vector(pl, p) >= 0) out.add(p)
    }
    return out
}

// https://stackoverflow.com/a/18472899
internal fun Polygon.isClockwise(): Boolean {
    var sum = 0.0
    for (i in points.indices) {
        val p1 = points[i]
        val p2 = points[getNextIndex(i, points)]
        sum += (p2.x - p1.x) * (p2.y + p1.y)
    }
    return sum < 0 // reversed Y axis
}

internal fun Polygon.normalize() =
    if (isClockwise()) Polygon(points.reversed()) else this

internal fun Polygon.lines() = sequence {
    for (i in points.indices) {
        val p1 = points[i]
        val p2 = points[getNextIndex(i, points)]
        yield(Pair(p1, p2))
    }
}

private fun vectorMultiplication(a: Point, b: Point, origin: Point): Int = Vector(origin, a) * Vector(origin, b)

internal fun intersects(p11: Point, p12: Point, p21: Point, p22: Point): Boolean {
    val t1 = vectorMultiplication(p22, p12, p11)
    val t2 = vectorMultiplication(p21, p12, p11)
    val d1 = vectorMultiplication(p22, p12, p21)
    val d2 = vectorMultiplication(p22, p11, p21)
    return t1 * t2 <= 0 && d1 * d2 <= 0
}

class GraphServiceImpl : GraphService {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun convertToVisibilityGraph(areaMap: AreaMap): VisibilityGraph {
        val polygons = areaMap.polygons.map { it.normalize() }
        val visibleNeighbours = polygons.flatMap { it.points }.associateWith { mutableSetOf<Point>() }

        val polygonPoints = polygons.associateWith { it.getConvexPoints() }
        for (pts1 in polygonPoints.values) {
            println("$pts1")
            for ((i, p1) in pts1.withIndex()) {
                val p1l = pts1[getPrevIndex(i, pts1)]
                val p1r = pts1[getNextIndex(i, pts1)]
                for (pts2 in polygonPoints.values) {
                    for (p2 in pts2) {
                        if (p1 == p2) continue
                        if (p2.isInsideObservationArea(p1l, p1, p1r)) continue

                        var foundIntersection = false
                        for ((p3, p4) in polygons.asSequence().flatMap { it.lines() }) {
                            if (setOf(p1, p2, p3, p4).size != 4) continue

                            if (intersects(p1, p2, p3, p4)) {
                                foundIntersection = true
                                println("($p1, $p2) intersects ($p3, $p4)")
                                break
                            }
                        }

                        if (!foundIntersection) {
                            visibleNeighbours[p1]?.add(p2)
                            visibleNeighbours[p2]?.add(p1)
                        }
                    }
                }
            }
        }

        return visibleNeighbours.toVisibilityGraph()
    }
}