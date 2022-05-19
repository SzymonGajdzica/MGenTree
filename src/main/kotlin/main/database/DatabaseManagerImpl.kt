package main.database

import main.data.FamilyLink
import main.data.Person

class DatabaseManagerImpl : DatabaseManager {

    private val graphDatabase: GraphDatabase = GraphDatabaseImpl()
    private val databaseMapper: DatabaseMapper = DatabaseMapperImpl()

    override fun getPersonList(): List<Person> {
        return graphDatabase.getAllNodes().map {
            databaseMapper.map(it)
        }.sortedBy { it.birthDate }
    }

    override fun updateFamilyLinks(familyLinkList: List<FamilyLink>) {
        graphDatabase.updateRelations(
            graphRelationList = familyLinkList.map {
                databaseMapper.map(it)
            }
        )
    }

    override fun getClosestFamilyLink(person1: Person, person2: Person): List<FamilyLink> {
        return graphDatabase.findClosestRelation(
            graphNode1 = databaseMapper.map(person1),
            graphNode2 = databaseMapper.map(person2),
        ).map {
            databaseMapper.map(it)
        }
    }

    override fun getAllFamilyLinks(person: Person): List<FamilyLink> {
        return graphDatabase.getAllRelations(
            graphNode = databaseMapper.map(person)
        ).map {
            databaseMapper.map(it)
        }
    }

    override fun close() {
        graphDatabase.close()
    }
}