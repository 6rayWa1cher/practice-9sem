package com.a6raywa1cher.graph

class GraphServiceImpl : GraphService {
    private fun vectMult(a: Point, b: Point, origin: Point): Int {
        val a1 = a.x - origin.x
        val a2 = a.y - origin.y
        val b1 = b.x - origin.x
        val b2 = b.y - origin.y
        return a1 * b2 - a2 * b1
    }

    private fun intersects(p11: Point, p12: Point, p21: Point, p22: Point): Boolean {
        val t1 = vectMult(p22, p12, p11)
        val t2 = vectMult(p21, p12, p11)
        val d1 = vectMult(p22, p12, p21)
        val d2 = vectMult(p22, p11, p21)
        return t1 * t2 <= 0 && d1 * d2 <= 0
    }

    override fun convertToVisibilityGraph(areaMap: AreaMap): VisibilityGraph {
        TODO("Not yet implemented")
    }
}