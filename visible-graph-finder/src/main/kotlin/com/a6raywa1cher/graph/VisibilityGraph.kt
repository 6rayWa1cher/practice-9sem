package com.a6raywa1cher.graph

data class VisibilityGraph(val nodes: List<Point> = listOf(), val neighbors: Map<Int, Set<Int>> = mapOf())

fun Map<Point, Set<Point>>.toVisibilityGraph(): VisibilityGraph {
    val nodes = this.keys.toList()
    val nodeToIndex = nodes.withIndex().associateBy({ it.value }, { it.index })
    val neighbors = this.entries.associateBy(
        { nodeToIndex[it.key]!! },
        { it.value.map { p -> nodeToIndex[p]!! }.toSet() }
    )
    return VisibilityGraph(nodes, neighbors)
}