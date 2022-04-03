package main.database

import main.data.GraphNode
import main.data.GraphRelation
import org.neo4j.graphdb.Node
import org.neo4j.graphdb.Relationship
import org.neo4j.graphdb.ResourceIterator
import java.util.regex.Pattern

fun Relationship.map(): GraphRelation {
    return GraphRelation(
        startNode = startNode.map(),
        endNode = endNode.map(),
        relationLabel = getProperty(GraphDatabaseConstant.relationLabelKey) as String
    )
}

fun GraphNode.map(node: Node): Node {
    node.setProperty(GraphDatabaseConstant.nodeIdKey, id)
    additionalData.forEach { (key, value) ->
        node.setProperty(key, value)
    }
    return node
}

fun Node.map(): GraphNode {
    return GraphNode(
        id = getProperty(GraphDatabaseConstant.nodeIdKey) as String,
        additionalData = allProperties
    )
}

fun String.extractIds(): List<Long> {
    val pattern = Pattern.compile("\\d+")
    val matcher = pattern.matcher(this)
    val resultList = arrayListOf<Long>()
    while (matcher.find()) {
        resultList.add(matcher.group().toLong())
    }
    return resultList
}

fun <T> ResourceIterator<T>.collect(): List<T> {
    return use {
        val result = arrayListOf<T>()
        it.forEach { data -> result.add(data) }
        result
    }
}