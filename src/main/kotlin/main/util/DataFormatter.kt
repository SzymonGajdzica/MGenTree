package main.util

import main.data.FamilyLink
import main.data.Person

interface DataFormatter {
    fun formatPerson(person: Person): String
    fun formatFamilyLink(familyLink: FamilyLink, startPerson: Person): String
    fun formatFamilyLinkList(familyLinkList: List<FamilyLink>): String
}