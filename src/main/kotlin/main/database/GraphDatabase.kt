package main.database

import main.data.GraphNode
import main.data.GraphRelation

interface GraphDatabase: AutoCloseable {
    fun getAllNodes(): List<GraphNode>
    fun updateRelations(graphRelationList: List<GraphRelation>)
    fun findClosestRelation(graphNode1: GraphNode, graphNode2: GraphNode): List<GraphRelation>
    fun getAllRelations(graphNode: GraphNode): List<GraphRelation>
}