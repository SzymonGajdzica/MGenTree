package main.link

import main.data.FamilyLink
import main.data.LinkedPerson

class FamilyLinksGeneratorImpl : FamilyLinksGenerator {

    private val providerHolder: FamilyLinkProviderHolder = FamilyLinkProviderHolderImpl()

    override fun getFamilyLinks(linkedPersonList: List<LinkedPerson>): List<FamilyLink> {
        val familyLinks = arrayListOf<FamilyLink>()
        linkedPersonList.forEach { person1 ->
            linkedPersonList.forEach { person2 ->
                val familyLink = getFamilyLink(person1, person2)
                if (familyLink != null)
                    familyLinks.add(familyLink)
            }
        }
        val reversedFamilyLinks = familyLinks.map { it.reversed() }
        return familyLinks.filter { link ->
            reversedFamilyLinks.any { link.person1 == it.person2 && link.person2 == it.person1 }
        }
    }

    override fun getFamilyLink(person1: LinkedPerson, person2: LinkedPerson): FamilyLink? {
        return providerHolder.providers.mapNotNull { it.provide(person1, person2) }.takeIf { it.isNotEmpty() }?.single()
    }

}