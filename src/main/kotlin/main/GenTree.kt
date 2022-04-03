package main

import main.data.LinkedPerson
import main.data.Person
import main.database.DatabaseManager
import main.database.DatabaseManagerImpl
import main.link.FamilyLinksGenerator
import main.link.FamilyLinksGeneratorImpl
import main.util.DataFormatter
import main.util.DataFormatterImpl
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

class GenTree {

    private val familyLinksGenerator: FamilyLinksGenerator = FamilyLinksGeneratorImpl()
    private val databaseManager: DatabaseManager = DatabaseManagerImpl()
    private val dataFormatter: DataFormatter = DataFormatterImpl()

    fun run() {
        updateDatabase()
        val personList = databaseManager.getPersonList()
        println("------------------------")
        printTwoPersonFamilyLinks(
            person1 = personList.single { it.firstName.startsWith("Mathew") },
            person2 = personList.single { it.firstName.startsWith("Maria") },
        )
        println("------------------------")
        printTwoPersonFamilyLinks(
            person1 = personList.single { it.firstName.startsWith("Maria") },
            person2 = personList.single { it.firstName.startsWith("Mathew") },
        )
        println("------------------------")
        printPersonFamilyLinks(
            person = personList.single { it.firstName.startsWith("Mathew") }
        )
        println("------------------------")
        personList.forEach {
            println(dataFormatter.formatPerson(it))
        }
        println("------------------------")
    }

    private fun printTwoPersonFamilyLinks(person1: Person, person2: Person) {
        databaseManager.getClosestFamilyLink(person1, person2).also {
            println(dataFormatter.formatFamilyLinkList(it))
        }
    }

    private fun printPersonFamilyLinks(person: Person) {
        databaseManager.getAllFamilyLinks(person).forEach {
            println(dataFormatter.formatFamilyLink(it, person))
        }
    }

    private fun updateDatabase() {
        val personList: List<LinkedPerson> = generatePersonList()
        val links = familyLinksGenerator.getFamilyLinks(personList)
        databaseManager.updateFamilyLinks(links)
    }

    private fun generatePersonList(): List<LinkedPerson> {
        val p1 = LinkedPerson.Builder(
            id = UUID.randomUUID().toString(),
            firstName = "Mathew",
            lastName = "Lester",
            male = true,
            birthDate = LocalDate.of(1978, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC),
        )
        val p2 = LinkedPerson.Builder(
            id = UUID.randomUUID().toString(),
            firstName = "Dominica",
            lastName = "Lester",
            male = false,
            birthDate = LocalDate.of(1978, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC),
        )
        val p3 = LinkedPerson.Builder(
            id = UUID.randomUUID().toString(),
            firstName = "Jan",
            lastName = "Lester",
            male = true,
            birthDate = LocalDate.of(1998, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC),
        )
        val p4 = LinkedPerson.Builder(
            id = UUID.randomUUID().toString(),
            firstName = "Julia",
            lastName = "Lester",
            male = false,
            birthDate = LocalDate.of(1998, 2, 1).atStartOfDay().toInstant(ZoneOffset.UTC),
        )
        val p5 = LinkedPerson.Builder(
            id = UUID.randomUUID().toString(),
            firstName = "Karol",
            lastName = "Lester",
            male = true,
            birthDate = LocalDate.of(1998, 3, 1).atStartOfDay().toInstant(ZoneOffset.UTC),
        )
        val p7 = LinkedPerson.Builder(
            id = UUID.randomUUID().toString(),
            firstName = "Maria",
            lastName = "Lester",
            male = false,
            birthDate = LocalDate.of(1998, 4, 1).atStartOfDay().toInstant(ZoneOffset.UTC),
        )
        val p6 = LinkedPerson.Builder(
            id = UUID.randomUUID().toString(),
            firstName = "Wladek",
            lastName = "Lester",
            male = true,
            birthDate = LocalDate.of(2008, 5, 1).atStartOfDay().toInstant(ZoneOffset.UTC),
        )
        p1.setSpouse(p2)
        p1.addChild(p3)
        p1.addChild(p4)
        p1.addChild(p5)
        p5.setSpouse(p7)
        p5.addChild(p6)
        val personBuilders = listOf(p1, p2, p3, p4, p5, p6, p7)
        return LinkedPerson.Builder.buildAll(personBuilders)
    }

}