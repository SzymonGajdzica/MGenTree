package main.data

data class GraphRelation(
    val startNode: GraphNode,
    val endNode: GraphNode,
    val relationLabel: String,
)