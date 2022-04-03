package main.database

import main.data.FamilyLink
import main.data.GraphNode
import main.data.GraphRelation
import main.data.Person

interface DatabaseManager: AutoCloseable {
    fun getPersonList(): List<Person>
    fun updateFamilyLinks(familyLinkList: List<FamilyLink>)
    fun getClosestFamilyLink(person1: Person, person2: Person): List<FamilyLink>
    fun getAllFamilyLinks(person: Person): List<FamilyLink>
}