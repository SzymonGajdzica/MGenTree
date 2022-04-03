package main.database

import main.data.GraphNode
import main.data.GraphRelation
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import org.neo4j.graphdb.*
import java.nio.file.Path

class GraphDatabaseImpl : GraphDatabase {

    private val databaseManager = DatabaseManagementServiceBuilder(Path.of("data"))
        .build()

    private val database: GraphDatabaseService
        get() = databaseManager.database(GraphDatabaseConstant.databaseName)

    override fun updateRelations(graphRelationList: List<GraphRelation>) {
        database.beginTx().use { transaction ->
            transaction.execute("MATCH (n) DETACH DELETE n")
            val idNode: Map<GraphNode, Node> = graphRelationList
                .flatMap {
                    listOf(it.startNode, it.endNode)
                }
                .toSet()
                .associateWith { graphNode: GraphNode ->
                    graphNode.map(transaction.createNode(GraphDatabaseConstant.baseNodeLabel))
                }
            graphRelationList.forEach {
                val startNode = requireNotNull(idNode[it.startNode])
                val endNode = requireNotNull(idNode[it.endNode])
                startNode.createRelationshipTo(endNode, GraphDatabaseConstant.baseRelationType).apply {
                    setProperty(GraphDatabaseConstant.relationLabelKey, it.relationLabel)
                }
            }
            transaction.commit()
        }
    }

    override fun findClosestRelation(graphNode1: GraphNode, graphNode2: GraphNode): List<GraphRelation> {
        return database.beginTx().use { transaction ->
            return@use transaction.execute(
                """
                    MATCH
                    (start:${GraphDatabaseConstant.baseNodeLabel.name()} {${GraphDatabaseConstant.nodeIdKey}: '${graphNode1.id}'}),
                    (end:${GraphDatabaseConstant.baseNodeLabel.name()} {${GraphDatabaseConstant.nodeIdKey}: '${graphNode2.id}'}),
                    p = shortestPath((start)-[*..100]-(end))
                    RETURN p
                """
            ).collect()
                .single()
                .values
                .single()
                .toString()
                .extractIds()
                .filterIndexed { index, _ -> index % 2 != 0 }
                .map {
                    transaction.getRelationshipById(it).map()
                }
        }
    }

    override fun getAllRelations(graphNode: GraphNode): List<GraphRelation> {
        return database.beginTx().use { transaction ->
            transaction.findNode(GraphDatabaseConstant.baseNodeLabel, GraphDatabaseConstant.nodeIdKey, graphNode.id)
                .relationships
                .map { it.map() }
        }
    }

    override fun getAllNodes(): List<GraphNode> {
        return database.beginTx().use { transaction ->
            transaction.allNodes.map {
                it.map()
            }
        }
    }

    override fun close() {
        databaseManager.shutdown()
    }



}