package main.ui

import main.data.LinkedPerson
import main.data.Person
import main.database.DatabaseManager
import main.database.DatabaseManagerImpl
import main.link.FamilyLinksGenerator
import main.link.FamilyLinksGeneratorImpl
import main.person.PersonParser
import main.person.PersonParserImpl
import main.util.DataFormatter
import main.util.DataFormatterImpl
import main.util.LocalStorage
import main.util.LocalStorageImpl
import java.util.*

class ConsoleViewControllerImpl: ConsoleViewController {

    private val familyLinksGenerator: FamilyLinksGenerator = FamilyLinksGeneratorImpl()
    private val databaseManager: DatabaseManager = DatabaseManagerImpl()
    private val dataFormatter: DataFormatter = DataFormatterImpl()
    private val personParser: PersonParser = PersonParserImpl()
    private val localStorage: LocalStorage = LocalStorageImpl()
    private val reader = Scanner(System.`in`)

    override fun run() {
        while (mainConsole()) {

        }
        println("Exiting")
        databaseManager.close()
    }

    private fun mainConsole(): Boolean {
        println("------------------------")
        println("1 to load new file")
        println("2 to print person list")
        println("3 to print connections between 2 people")
        println("4 to print all connections of people")
        println("5 to exit")
        val input = reader.next().trim().toIntOrNull()
        when(input) {
            1 -> loadNewFile()
            2 -> printPersonList()
            3 -> print2PeopleConnections()
            4 -> printAllPersonConnections()
        }
        return input != 5
    }

    private fun loadNewFile() {
        println("------------------------")
        println("Enter new file path")
        val filePath = reader.next().trim()
        runCatching {
            val rawPersonList = localStorage.loadRawPersonList(filePath)
            val personList: List<LinkedPerson> = personParser.parseRawPersonList(rawPersonList)
            val links = familyLinksGenerator.getFamilyLinks(personList)
            databaseManager.updateFamilyLinks(links)
        }.getOrElse {
            println("Failed to load new file at path $filePath cause error ${it.message}")
        }
    }

    private fun printPersonList() {
        val personList = databaseManager.getPersonList()
        println("------------------------")
        personList.forEach {
            println(dataFormatter.formatPerson(it))
        }
    }

    private fun print2PeopleConnections() {
        println("------------------------")
        val person1 = askForPerson() ?: return
        val person2 = askForPerson() ?: return
        databaseManager.getClosestFamilyLink(person1, person2).also {
            println(dataFormatter.formatFamilyLinkList(it))
        }
    }

    private fun printAllPersonConnections() {
        println("------------------------")
        val person = askForPerson() ?: return
        databaseManager.getAllFamilyLinks(person).forEach {
            println(dataFormatter.formatFamilyLink(it, person))
        }
    }

    private fun askForPerson(): Person? {
        val personList = databaseManager.getPersonList()
        println("Enter person firstname and surname")
        val name = reader.next().trim()
        return personList.firstOrNull { "${it.firstName} ${it.lastName}".startsWith(name, ignoreCase = true) }.also {
            if (it == null)
                println("Could not find person with name $name")
        }
    }

}