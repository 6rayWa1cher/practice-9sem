package com.a6raywa1cher.graph

import kotlin.test.Test
import kotlin.test.assertEquals

class GraphServiceImplTest {
    private val graphService = GraphServiceImpl()

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
            listOf(
                Point(0, 0)
            ),
            mapOf(
                0 to setOf()
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
            listOf(
                Point(0, 0),
                Point(10, 10)
            ),
            mapOf(
                0 to setOf(1),
                1 to setOf(0)
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
            listOf(
                Point(0, 0),
                Point(0, 10),
                Point(10, 0),
                Point(10, 10)
            ),
            mapOf(
                0 to setOf(1, 2, 3),
                1 to setOf(0, 2, 3),
                2 to setOf(1, 0, 3),
                3 to setOf(1, 2, 0)
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
                        Point(5, 3),
                        Point(6, 2),
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            listOf(
                Point(3, 1),
                Point(3, 3),
                Point(2, 2),
                Point(5, 1),
                Point(5, 3),
                Point(6, 2)
            ),
            mapOf(
                0 to setOf(2, 1, 4, 3),
                1 to setOf(2, 0, 3, 4),
                2 to setOf(0, 1),
                3 to setOf(0, 1, 4, 5),
                4 to setOf(3, 0, 1, 5),
                5 to setOf(3, 4)
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testTwoCubicPolygons() {
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
            listOf(
                Point(0, 0),    // 0
                Point(0, 1),    // 1
                Point(1, 0),    // 2
                Point(1, 1),    // 3
                Point(10, 0),   // 4
                Point(10, 1),   // 5
                Point(11, 0),   // 6
                Point(11, 1)    // 7
            ),
            mapOf(
                0 to setOf(1, 2),
                1 to setOf(0, 3),
                2 to setOf(0, 3, 4, 5),
                3 to setOf(1, 2, 4, 5),
                4 to setOf(2, 3, 5, 6),
                5 to setOf(4, 2, 3, 7),
                6 to setOf(4, 7),
                7 to setOf(5, 6)
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testRussianLetterP() {
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(0, 0),
                        Point(0, 3),
                        Point(1, 3),
                        Point(1, 1),
                        Point(2, 1),
                        Point(2, 3),
                        Point(3, 3),
                        Point(3, 0)
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            listOf(
                Point(0, 0),
                Point(0, 3),
                Point(1, 3),
                Point(1, 1),
                Point(2, 1),
                Point(2, 3),
                Point(3, 3),
                Point(3, 0)
            ),
            mapOf(
                0 to setOf(1, 7),
                1 to setOf(0, 2),
                2 to setOf(3, 4, 5),
                3 to setOf(4, 5, 2),
                4 to setOf(3, 2, 5),
                5 to setOf(2, 3, 4),
                6 to setOf(5, 7),
                7 to setOf(0, 6)
            )
        )

        assertEquals(expected, visibilityGraph)
    }
}