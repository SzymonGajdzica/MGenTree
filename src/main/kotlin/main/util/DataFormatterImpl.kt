package main.util

import kotlinx.datetime.Clock
import kotlinx.datetime.toDateTimePeriod
import main.data.FamilyLink
import main.data.Person
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class DataFormatterImpl : DataFormatter {

    override fun formatPerson(person: Person): String {
        val age = (Clock.System.now() - person.birthDate).inWholeDays / 365
        return buildString {
            append(person.firstName)
            append(" ")
            append(person.lastName)
            append(" (")
            append(age)
            append(")")
        }
    }

    override fun formatFamilyLink(familyLink: FamilyLink, startPerson: Person): String {
        val mFamilyLink = if(familyLink.person1 == startPerson) familyLink else familyLink.reversed()
        require(mFamilyLink.person1 == startPerson)
        return mFormatFamilyLink(mFamilyLink, true)
    }

    private fun mFormatFamilyLink(familyLink: FamilyLink, withFirstPerson: Boolean): String {
        return buildString {
            if(withFirstPerson) {
                append(formatPerson(familyLink.person1))
                append(" ")
            }
            append("is ")
            append(familyLink.name)
            append(" of ")
            append(formatPerson(familyLink.person2))
        }
    }

    override fun formatFamilyLinkList(familyLinkList: List<FamilyLink>): String {
        var oldPerson: Person? = findFirstPerson(familyLinkList)
        var firstPerson = true
        return buildString {
            familyLinkList.forEach {
                if(!firstPerson)
                    append(" ")
                val familyLink = if(oldPerson == it.person1) it else it.reversed()
                require(oldPerson == familyLink.person1)
                append(mFormatFamilyLink(familyLink, firstPerson))
                oldPerson = familyLink.person2
                firstPerson = false
            }
        }
    }

    private fun findFirstPerson(familyLinkList: List<FamilyLink>): Person {
        if(familyLinkList.size == 1)
            return familyLinkList.single().person1
        val firstLink = familyLinkList[0]
        val secondLink = familyLinkList[1]
        return if(firstLink.person1 == secondLink.person1 || firstLink.person1 == secondLink.person2)
            firstLink.person2
        else
            firstLink.person1
    }

}