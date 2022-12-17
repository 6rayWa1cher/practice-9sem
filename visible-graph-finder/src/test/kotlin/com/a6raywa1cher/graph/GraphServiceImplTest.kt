package com.a6raywa1cher.graph

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
}