package main.database

import org.neo4j.graphdb.Label
import org.neo4j.graphdb.RelationshipType

object GraphDatabaseConstant {

    const val databaseName = "neo4j"
    const val nodeIdKey = "m_id"
    const val relationLabelKey = "m_label"
    val baseRelationType: RelationshipType = RelationshipType.withName("baseRelationType")
    val baseNodeLabel: Label = Label.label("baseLabel")

}