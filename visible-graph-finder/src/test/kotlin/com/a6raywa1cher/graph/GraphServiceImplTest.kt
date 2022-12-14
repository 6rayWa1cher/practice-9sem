package com.a6raywa1cher.graph

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class GraphServiceImplTest {
    private val graphService = GraphServiceImpl()

    private fun VisibilityGraph.normalize(areaMap: AreaMap): VisibilityGraph {
        val visibleNeighbours = areaMap.polygons.flatMap { it.points }.associateWith { mutableSetOf<Point>() }
        for ((p1, localNeighbors) in neighbors.entries) {
            localNeighbors.forEach { visibleNeighbours[p1]!!.add(it); visibleNeighbours[it]!!.add(p1); }
        }
        return copy(neighbors = visibleNeighbours)
    }

    @Test
    fun testEmpty() {
        val areaMap = AreaMap()
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph()

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testOnePolygonWithOnePoint() {
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(0, 0)
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            mapOf(
                Pair(Point(0, 0), setOf())
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testTwoPolygonsWithOnePointEach() {
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(0, 0)
                    )
                ),
                Polygon(
                    listOf(
                        Point(10, 10)
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            mapOf(
                Pair(Point(0, 0), setOf(Point(10, 10))),
                Pair(Point(10, 10), setOf(Point(0, 0)))
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testTwoPolygonsWithTwoPointsEach() {
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(0, 0),
                        Point(0, 10)
                    )
                ),
                Polygon(
                    listOf(
                        Point(10, 0),
                        Point(10, 10)
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            mapOf(
                Pair(Point(0, 0), setOf(Point(10, 10), Point(10, 0), Point(0, 10))),
                Pair(Point(10, 0), setOf(Point(10, 10), Point(0, 0), Point(0, 10))),
                Pair(Point(0, 10), setOf(Point(10, 10), Point(10, 0), Point(0, 0))),
                Pair(Point(10, 10), setOf(Point(0, 10), Point(10, 0), Point(0, 0))),
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testTwoTriangularPolygons() {
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(3, 1),
                        Point(3, 3),
                        Point(2, 2)
                    )
                ),
                Polygon(
                    listOf(
                        Point(5, 1),
                        Point(6, 2),
                        Point(5, 3)
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            mapOf(
                Point(3, 1) to setOf(Point(5, 1), Point(5, 3), Point(3, 3), Point(2, 2)),
                Point(3, 3) to setOf(Point(2, 2), Point(5, 3), Point(5, 1)),
                Point(5, 1) to setOf(Point(5, 3), Point(6, 2)),
                Point(5, 3) to setOf(Point(6, 2))
            )
        ).normalize(areaMap)

        assertEquals(expected, visibilityGraph)
        assertFalse(visibilityGraph.neighbors[Point(2, 2)]!!.contains(Point(6, 2)))
        assertFalse(visibilityGraph.neighbors[Point(6, 2)]!!.contains(Point(2, 2)))
    }

    @Test
    fun testTwoCubicPolygons() {
        TODO()
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(0, 0),
                        Point(0, 1),
                        Point(1, 0),
                        Point(1, 1)
                    )
                ),
                Polygon(
                    listOf(
                        Point(10, 0),
                        Point(10, 1),
                        Point(11, 0),
                        Point(11, 1)
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            mapOf(
//                Point(0, 0) to
            )
        ).normalize(areaMap)

        assertEquals(expected, visibilityGraph)
        assertFalse(visibilityGraph.neighbors[Point(2, 2)]!!.contains(Point(6, 2)))
        assertFalse(visibilityGraph.neighbors[Point(6, 2)]!!.contains(Point(2, 2)))
    }
}