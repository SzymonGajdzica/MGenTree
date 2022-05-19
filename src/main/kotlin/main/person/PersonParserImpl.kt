package main.person

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import main.data.LinkedPerson

class PersonParserImpl : PersonParser {

    private val personValidator: PersonValidator = PersonValidatorImpl()

    override fun parseRawPersonList(rawPersonList: List<String>): List<LinkedPerson> {
        val personBuilders: HashMap<String, LinkedPerson.Builder> = hashMapOf()
        val slicedList = rawPersonList.map { it.split(";") }
        slicedList.forEach { sliced ->
            personBuilders[sliced[0]] = LinkedPerson.Builder(
                id = sliced[0],
                male = sliced[3].toInt() == 1,
                birthDate = Instant.parse(sliced[4]),
                firstName = sliced[1],
                lastName = sliced[2],
            )
        }
        slicedList.forEach { sliced ->
            val person = personBuilders.getValue(sliced[0])
            personBuilders[sliced[5]]?.addChild(person)
            personBuilders[sliced[6]]?.addChild(person)
            personBuilders[sliced[7]]?.setSpouse(person)
        }
        return LinkedPerson.Builder.buildAll(personBuilders.values).onEach { personValidator.validate(it) }
    }

}