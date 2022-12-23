package com.a6raywa1cher.graph

import kotlin.test.*

class GraphServiceImplTest {
    private val graphService = GraphServiceImpl()

    @Test
    fun testIntersects__false1() {
        val p1 = Point(0, 0)
        val p2 = Point(0, 1)
        val p3 = Point(10, 0)
        val p4 = Point(11, 0)
        assertFalse(intersects(p1, p2, p3, p4))
        assertFalse(intersects(p2, p1, p3, p4))
        assertFalse(intersects(p1, p2, p4, p3))
        assertFalse(intersects(p2, p1, p4, p3))
    }

    @Test
    fun testIntersects__false2() {
        val p1 = Point(0, 0)
        val p2 = Point(1, 0)
        val p3 = Point(10, 0)
        val p4 = Point(11, 0)
        assertFalse(intersects(p1, p2, p3, p4))
        assertFalse(intersects(p2, p1, p3, p4))
        assertFalse(intersects(p1, p2, p4, p3))
        assertFalse(intersects(p2, p1, p4, p3))
    }

    @Test
    fun testIntersects__false3() {
        val p1 = Point(0, 0)
        val p2 = Point(2, 2)
        val p3 = Point(3, 3)
        val p4 = Point(5, 5)
        assertFalse(intersects(p1, p2, p3, p4))
        assertFalse(intersects(p2, p1, p3, p4))
        assertFalse(intersects(p1, p2, p4, p3))
        assertFalse(intersects(p2, p1, p4, p3))
    }

    @Test
    fun testIntersects__false4() {
        val p1 = Point(0, 0)
        val p2 = Point(0, 2)
        val p3 = Point(1, 0)
        val p4 = Point(1, 2)
        assertFalse(intersects(p1, p2, p3, p4))
        assertFalse(intersects(p2, p1, p3, p4))
        assertFalse(intersects(p1, p2, p4, p3))
        assertFalse(intersects(p2, p1, p4, p3))
    }

    @Test
    fun testIntersects__false5() {
        val p1 = Point(256, 128)
        val p2 = Point(197, 385)
        val p3 = Point(775, 259)
        val p4 = Point(731, 66)
        assertFalse(intersects(p1, p2, p3, p4))
        assertFalse(intersects(p2, p1, p3, p4))
        assertFalse(intersects(p1, p2, p4, p3))
        assertFalse(intersects(p2, p1, p4, p3))
    }

    @Test
    fun testIntersects__false6() {
        val p1 = Point(x = 256, y = 128)
        val p2 = Point(x = 768, y = 384)
        val p3 = Point(x = 340, y = 493)
        val p4 = Point(x = 374, y = 371)
        assertFalse(intersects(p1, p2, p3, p4))
        assertFalse(intersects(p2, p1, p3, p4))
        assertFalse(intersects(p1, p2, p4, p3))
        assertFalse(intersects(p2, p1, p4, p3))
    }

    @Test
    fun testIntersects__true1() {
        val p1 = Point(0, 0)
        val p2 = Point(0, 1)
        val p3 = Point(-10, 0)
        val p4 = Point(11, 0)
        assertTrue(intersects(p1, p2, p3, p4))
        assertTrue(intersects(p2, p1, p3, p4))
        assertTrue(intersects(p1, p2, p4, p3))
        assertTrue(intersects(p2, p1, p4, p3))
    }

    @Test
    fun testIntersects__true2() {
        val p1 = Point(0, 0)
        val p2 = Point(1, 0)
        val p3 = Point(-10, 0)
        val p4 = Point(10, 0)
        assertTrue(intersects(p1, p2, p3, p4))
        assertTrue(intersects(p2, p1, p3, p4))
        assertTrue(intersects(p1, p2, p4, p3))
        assertTrue(intersects(p2, p1, p4, p3))
    }

    @Test
    fun testIsClockwise1() {
        val p1 = Polygon(
            listOf(
                Point(3, 1),
                Point(3, 3),
                Point(2, 2)
            )
        )
        assertTrue(p1.isClockwise())
    }

    @Test
    fun testIsClockwise2() {
        val p = Polygon(
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
        assertFalse(p.isClockwise())
    }

    @Test
    fun testIsInsideObservationArea1() {
        val pl = Point(3, 0)
        val p = Point(0, 0)
        val pr = Point(0, 3)
        assertTrue(Point(1, 1).isInsideObservationArea(pl, p, pr))
        assertTrue(Point(2, 1).isInsideObservationArea(pl, p, pr))
        assertTrue(Point(3, 1).isInsideObservationArea(pl, p, pr))
        assertTrue(Point(4, 1).isInsideObservationArea(pl, p, pr))
        assertTrue(Point(1, 3).isInsideObservationArea(pl, p, pr))
        assertFalse(Point(-1, -1).isInsideObservationArea(pl, p, pr))
        assertFalse(Point(2, -1).isInsideObservationArea(pl, p, pr))
        assertFalse(pl.isInsideObservationArea(pl, p, pr))
        assertFalse(pr.isInsideObservationArea(pl, p, pr))
    }

    @Test
    fun testIsInsideObservationArea2() {
        val pl = Point(0, 3)
        val p = Point(1, 3)
        val pr = Point(1, 2)
        assertTrue(Point(0, 0).isInsideObservationArea(pl, p, pr))
        assertFalse(Point(2, 3).isInsideObservationArea(pl, p, pr))
        assertFalse(pl.isInsideObservationArea(pl, p, pr))
        assertFalse(pr.isInsideObservationArea(pl, p, pr))
    }

    @Test
    fun testIsInsideObservationAreaAntiCollinear() {
        val pl = Point(3, 0)
        val p = Point(2, 0)
        val pr = Point(0, 0)
        assertTrue(Point(3, 3).isInsideObservationArea(pl, p, pr))
        assertFalse(Point(3, -3).isInsideObservationArea(pl, p, pr))
        assertFalse(pl.isInsideObservationArea(pl, p, pr))
        assertFalse(pr.isInsideObservationArea(pl, p, pr))
    }

    @Test
    fun testIsInsideObservationAreaCollinear() {
        val pl = Point(1, 0)
        val p = Point(2, 0)
        val pr = Point(1, 0)
        assertFalse(Point(3, 3).isInsideObservationArea(pl, p, pr))
        assertFalse(Point(3, -3).isInsideObservationArea(pl, p, pr))
        assertFalse(Point(0, 0).isInsideObservationArea(pl, p, pr))
        assertFalse(Point(3, 0).isInsideObservationArea(pl, p, pr))
        for (i in -3L..3L) {
            for (j in -3L..3L) {
                assertFalse(Point(i, j).isInsideObservationArea(pl, p, pr))
            }
        }
        assertFalse(pl.isInsideObservationArea(pl, p, pr))
        assertFalse(pr.isInsideObservationArea(pl, p, pr))
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
                Point(x = 2, y = 2),
                Point(x = 3, y = 3),
                Point(x = 3, y = 1),
                Point(x = 5, y = 1),
                Point(x = 5, y = 3),
                Point(x = 6, y = 2)
            ),
            mapOf(
                0 to setOf(2, 1),
                1 to setOf(0, 2, 3, 4),
                2 to setOf(0, 1, 3, 4),
                3 to setOf(2, 1, 4, 5),
                4 to setOf(1, 2, 3, 5),
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
                        Point(1, 1),
                        Point(1, 0),
                    )
                ),
                Polygon(
                    listOf(
                        Point(10, 0),
                        Point(10, 1),
                        Point(11, 1),
                        Point(11, 0),
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            listOf(
                Point(0, 0),
                Point(0, 1),
                Point(1, 1),
                Point(1, 0),
                Point(10, 0),
                Point(10, 1),
                Point(11, 1),
                Point(11, 0),
            ),
            mapOf(
                0 to setOf(1, 3),
                1 to setOf(0, 2),
                2 to setOf(1, 3, 5, 4),
                3 to setOf(0, 2, 4, 5),
                4 to setOf(3, 2, 5, 7),
                5 to setOf(4, 3, 2, 6),
                6 to setOf(7, 5),
                7 to setOf(4, 6)
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testCubicWithProbe() {
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(0, 0),
                        Point(0, 1),
                        Point(1, 1),
                        Point(1, 0),
                    )
                ),
                Polygon(
                    listOf(
                        Point(10, 10),
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            listOf(
                Point(0, 0),
                Point(0, 1),
                Point(1, 1),
                Point(1, 0),
                Point(10, 10),
            ),
            mapOf(
                0 to setOf(3, 1),
                1 to setOf(0, 2, 4),
                2 to setOf(1, 3, 4),
                3 to setOf(0, 2, 4),
                4 to setOf(1, 2, 3),
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
                2 to setOf(1, 5),
                3 to setOf(),
                4 to setOf(),
                5 to setOf(2, 6),
                6 to setOf(5, 7),
                7 to setOf(0, 6)
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testRussianLetterPWithExplicitPointOnOneEdge() {
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
                        Point(3, 0),
                        Point(2, 0)
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
                Point(3, 0),
                Point(2, 0)
            ),
            mapOf(
                0 to setOf(1, 7, 8),
                1 to setOf(0, 2),
                2 to setOf(1, 5),
                3 to setOf(),
                4 to setOf(),
                5 to setOf(2, 6),
                6 to setOf(5, 7),
                7 to setOf(0, 6, 8),
                8 to setOf(0, 7)
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testSlimWallCase() {
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(1, 0),
                        Point(1, 1),
                        Point(0, 1),
                        Point(1, 1),
                        Point(1, 2),
                        Point(2, 2),
                        Point(2, 1),
                        Point(3, 1),
                        Point(2, 1),
                        Point(2, 0)
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            listOf(
                Point(1, 0),
                Point(1, 1),
                Point(0, 1),
                Point(1, 2),
                Point(2, 2),
                Point(2, 1),
                Point(3, 1),
                Point(2, 0)
            ),
            mapOf(
                0 to setOf(2, 7),
                1 to setOf(),
                2 to setOf(0, 3),
                3 to setOf(2, 4),
                4 to setOf(3, 6),
                5 to setOf(),
                6 to setOf(7, 4),
                7 to setOf(0, 6),
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    @Ignore
    fun testSelfIntersectionWithProbe() {
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(0, 0),
                        Point(2, 2),
                        Point(0, 4),
                        Point(3, 4),
                        Point(1, 2),
                        Point(3, 0),
                    )
                ),
                Polygon(
                    listOf(
                        Point(4, 2)
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            listOf(
                Point(0, 0),
                Point(2, 2),
                Point(0, 4),
                Point(3, 4),
                Point(1, 2),
                Point(3, 0),
                Point(4, 2)
            ),
            mapOf(
                0 to setOf(5, 4, 2),
                1 to setOf(5, 3, 6),
                2 to setOf(0, 4, 3),
                3 to setOf(1, 5, 6, 2),
                4 to setOf(0, 2),
                5 to setOf(0, 1, 3, 6),
                6 to setOf(5, 1, 3)
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testRealCase1() {
        val areaMap = AreaMap(
            listOf(
                Polygon(listOf(Point(256, 128))),
                Polygon(listOf(Point(197, 385), Point(340, 493), Point(374, 371))),
                Polygon(listOf(Point(510, 137), Point(731, 66), Point(775, 259), Point(479, 220))),
                Polygon(listOf(Point(768, 384))),
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            listOf(
                Point(x = 256, y = 128),

                Point(x = 197, y = 385),
                Point(x = 340, y = 493),
                Point(x = 374, y = 371),

                Point(x = 479, y = 220),
                Point(x = 775, y = 259),
                Point(x = 731, y = 66),
                Point(x = 510, y = 137),

                Point(x = 768, y = 384)
            ),
            mapOf(
                0 to setOf(1, 3, 4, 6, 7, 8),
                1 to setOf(0, 2, 3, 4, 5, 7),
                2 to setOf(1, 3, 4, 5, 7, 8),
                3 to setOf(0, 1, 2, 4, 5, 7, 8),
                4 to setOf(0, 1, 2, 3, 5, 7, 8),
                5 to setOf(1, 2, 3, 4, 6, 8),
                6 to setOf(0, 5, 7),
                7 to setOf(0, 1, 2, 3, 4, 6),
                8 to setOf(0, 2, 3, 4, 5)
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    fun testOneCubeOnAnotherBigger() {
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(0, 2),
                    )
                ),
                Polygon(
                    listOf(
                        Point(5, 2)
                    )
                ),
                Polygon(
                    listOf(
                        Point(2, 0),
                        Point(2, 2),
                        Point(3, 2),
                        Point(3, 0)
                    )
                ),
                Polygon(
                    listOf(
                        Point(1, 2),
                        Point(1, 4),
                        Point(4, 4),
                        Point(4, 2)
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            listOf(
                Point(0, 2),
                Point(5, 2),
                Point(2, 0),
                Point(2, 2),
                Point(3, 2),
                Point(3, 0),
                Point(1, 2),
                Point(1, 4),
                Point(4, 4),
                Point(4, 2)
            ),
            mapOf(
                0 to setOf(2, 6, 7),
                1 to setOf(5, 8, 9),
                2 to setOf(0, 5, 6),
                3 to setOf(6),
                4 to setOf(9),
                5 to setOf(1, 2, 9),
                6 to setOf(0, 2, 3, 7),
                7 to setOf(0, 6, 8),
                8 to setOf(1, 7, 9),
                9 to setOf(1, 4, 5, 8)
            )
        )

        assertEquals(expected, visibilityGraph)
    }

    @Test
    @Ignore
    fun testOneCubeOnAnotherShifted() {
        val areaMap = AreaMap(
            listOf(
                Polygon(
                    listOf(
                        Point(0, 2),
                    )
                ),
                Polygon(
                    listOf(
                        Point(6, 2)
                    )
                ),
                Polygon(
                    listOf(
                        Point(3, 0),
                        Point(3, 2),
                        Point(5, 2),
                        Point(5, 0)
                    )
                ),
                Polygon(
                    listOf(
                        Point(1, 2),
                        Point(1, 4),
                        Point(4, 4),
                        Point(4, 2)
                    )
                )
            )
        )
        val visibilityGraph = graphService.convertToVisibilityGraph(areaMap)

        val expected = VisibilityGraph(
            listOf(
                Point(0, 2),
                Point(6, 2),
                Point(3, 0),
                Point(3, 2),
                Point(5, 2),
                Point(5, 0),
                Point(1, 2),
                Point(1, 4),
                Point(4, 4),
                Point(4, 2)
            ),
            mapOf(
                0 to setOf(2, 6, 7),
                1 to setOf(4, 5, 8),
                2 to setOf(0, 5, 6),
                3 to setOf(6),
                4 to setOf(1, 5, 8, 9),
                5 to setOf(1, 2, 4),
                6 to setOf(0, 2, 3, 7),
                7 to setOf(0, 6, 8),
                8 to setOf(1, 4, 7),
                9 to setOf(8)
            )
        )

        assertEquals(expected, visibilityGraph)
    }
}