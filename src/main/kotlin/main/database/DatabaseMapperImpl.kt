package main.database

import main.data.*
import main.link.FamilyLinkProviderHolder
import main.link.FamilyLinkProviderHolderImpl
import java.time.Instant

class DatabaseMapperImpl : DatabaseMapper {

    private val familyLinkProviderHolder: FamilyLinkProviderHolder = FamilyLinkProviderHolderImpl()
    companion object {
        private const val firstNameKey = "firstNameKey"
        private const val lastNameKey = "lastNameKey"
        private const val birthDateKey = "birthDate"
        private const val maleKey = "male"
    }

    override fun map(graphRelation: GraphRelation): FamilyLink {
        return familyLinkProviderHolder.providers.mapNotNull {
            it.provide(graphRelation.relationLabel, map(graphRelation.startNode), map(graphRelation.endNode))
        }.single()
    }

    override fun map(familyLink: FamilyLink): GraphRelation {
        return GraphRelation(
            startNode = map(familyLink.person1),
            endNode = map(familyLink.person2),
            relationLabel = familyLink.label,
        )
    }

    override fun map(graphNode: GraphNode): Person {
        return BasePerson(
            id = graphNode.id,
            firstName = graphNode.additionalData[firstNameKey] as String,
            lastName = graphNode.additionalData[lastNameKey] as String,
            male = graphNode.additionalData[maleKey] as Boolean,
            birthDate = (graphNode.additionalData[birthDateKey] as String).let {
                Instant.parse(it)
            },
        )
    }

    override fun map(person: Person): GraphNode {
        return GraphNode(
            id = person.id,
            additionalData = mapOf(
                Pair(firstNameKey, person.firstName),
                Pair(lastNameKey, person.lastName),
                Pair(maleKey, person.male),
                Pair(birthDateKey, person.birthDate.toString()),
            )
        )
    }

}