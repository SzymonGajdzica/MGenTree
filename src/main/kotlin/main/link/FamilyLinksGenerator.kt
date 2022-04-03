package main.link

import main.data.FamilyLink
import main.data.LinkedPerson

interface FamilyLinksGenerator {
    fun getFamilyLinks(linkedPersonList: List<LinkedPerson>): List<FamilyLink>
    fun getFamilyLink(person1: LinkedPerson, person2: LinkedPerson): FamilyLink?
}