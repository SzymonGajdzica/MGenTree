package main.database

import main.data.GraphRelation
import main.data.FamilyLink
import main.data.GraphNode
import main.data.Person

interface DatabaseMapper {
    fun map(graphRelation: GraphRelation): FamilyLink
    fun map(familyLink: FamilyLink): GraphRelation
    fun map(graphNode: GraphNode): Person
    fun map(person: Person): GraphNode
}