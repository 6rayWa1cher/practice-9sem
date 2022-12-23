package com.a6raywa1cher.graph

import org.slf4j.LoggerFactory
import java.lang.Math.floorMod
import java.util.concurrent.ForkJoinPool
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

internal data class Vector(val x: Long, val y: Long) {
    constructor(from: Point, to: Point) : this(to.x - from.x, to.y - from.y)

    fun length(): Double = sqrt((x * x + y * y).toDouble())

    operator fun times(other: Vector): Long = this.x * other.y - this.y * other.x
}

internal fun Vector.isCollinear(other: Vector) = this.x * other.x >= 0 && this.y * other.y >= 0

internal fun Point.isInsideObservationArea(pl: Point, p: Point, pr: Point): Boolean {
    val v1 = Vector(p, pl)
    val v2 = Vector(p, pr)
    val v3 = Vector(p, this)
    if (v1 * v2 == 0L) {
        val va = Vector(pl, p)
        val vb = Vector(p, pr)
        if (!va.isCollinear(vb)) return false
        val vd = Vector(pl, this)
        val ve = Vector(p, this)
        return va * vd < 0 && vb * ve < 0
    }
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
        val t = Vector(pl, pr) * Vector(pl, p)
        if (t >= 0) out.add(p)
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

inline fun intersect_1(_a: Long, _b: Long, _c: Long, _d: Long): Boolean {
    var a = _a
    var b = _b
    var c = _c
    var d = _d
    if (a > b) {
        val tmp = a
        a = b
        b = tmp
    }
    if (c > d) {
        val tmp = c
        c = d
        d = tmp
    }
    return max(a, c) <= min(b, d)
}

inline fun det(a: Long, b: Long, c: Long, d: Long) = a * d - b * c

const val e = 1e-9

inline fun between(a: Long, b: Long, c: Double) = min(a, b) <= c + e && c <= max(a, b) + e

internal fun intersects(a: Point, b: Point, c: Point, d: Point): Boolean {
    val a1 = a.y - b.y
    val b1 = b.x - a.x
    val c1 = -a1 * a.x - b1 * a.y
    val a2 = c.y - d.y
    val b2 = d.x - c.x
    val c2 = -a2 * c.x - b2 * c.y
    val zn = det(a1, b1, a2, b2)
    return if (zn != 0L) {
        val x = -det(c1, b1, c2, b2) * 1.toDouble() / zn
        val y = -det(a1, c1, a2, c2) * 1.toDouble() / zn
        (between(a.x, b.x, x) && between(a.y, b.y, y)
                && between(c.x, d.x, x) && between(c.y, d.y, y))
    } else {
        (det(a1, c1, a2, c2) == 0L && det(b1, c1, b2, c2) == 0L
                && intersect_1(a.x, b.x, c.x, d.x)
                && intersect_1(a.y, b.y, c.y, d.y))
    }
}

class GraphServiceImpl : GraphService {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val pool = ForkJoinPool(8)
    private val useMultithreading = true

    private fun processPolygon(
        p1i: Int,
        polygonEntries: List<Map.Entry<Polygon, Collection<Point>>>,
        visibleNeighbours: Map<Point, MutableCollection<Point>>,
        polygons: List<Polygon>
    ) {
        val e1 = polygonEntries[p1i]
        val polygon1 = e1.key
        val pts1 = e1.value
//        logger.trace("$pts1")
        for ((i, p1) in polygon1.points.withIndex()) {
            if (p1 !in pts1) continue
            val p1l = polygon1.points[getPrevIndex(i, polygon1.points)]
            val p1r = polygon1.points[getNextIndex(i, polygon1.points)]
            for ((p2i, e2) in polygonEntries.withIndex()) {
                if (p2i < p1i) continue
                val pts2 = e2.value
                for (p2 in pts2) {
                    if (p1 == p2) continue
                    if (p2.isInsideObservationArea(p1l, p1, p1r)) continue

                    if (p1 in visibleNeighbours[p1]!!) continue

                    var foundIntersection = false
                    for ((p3, p4) in polygons.asSequence().flatMap { it.lines() }) {
                        if (setOf(p1, p2, p3, p4).size != 4) continue

                        if (intersects(p1, p2, p3, p4)) {
                            foundIntersection = true
//                            logger.trace("($p1, $p2) intersects ($p3, $p4)")
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

    override fun convertToVisibilityGraph(areaMap: AreaMap): VisibilityGraph {
        logger.trace("got req")
        val polygons = areaMap.polygons.map { it.normalize() }
        val visibleNeighbours = polygons.flatMap { it.points }.associateWith { mutableSetOf<Point>() }

        val polygonPoints = polygons.associateWith { it.getConvexPoints().toSet() }
        val polygonEntries = polygonPoints.entries.toList()


        if (useMultithreading) {
            val tasks = polygonEntries.indices.map { i ->
                pool.submit {
                    processPolygon(
                        i,
                        polygonEntries,
                        visibleNeighbours,
                        polygons
                    )
                }
            }
            tasks.forEach { it.get() }
        } else {
            for (i in polygonEntries.indices) {
                processPolygon(i, polygonEntries, visibleNeighbours, polygons)
            }
        }

        logger.trace("completed")

        return visibleNeighbours.toVisibilityGraph()
    }
}