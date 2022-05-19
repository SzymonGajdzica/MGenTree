package main.person

import kotlinx.datetime.Clock
import main.data.LinkedPerson
import main.util.DataFormatter
import main.util.DataFormatterImpl

class PersonValidatorImpl : PersonValidator {

    private val dataFormatter: DataFormatter = DataFormatterImpl()

    override fun validate(person: LinkedPerson) {
        require(person.birthDate < Clock.System.now()) {
            getErrorMessage(person, null, "birthDate cannot be bigger than now")
        }
        require(person.firstName.isNotBlank()) {
            getErrorMessage(person, null, "firstName cannot be blank")
        }
        require(person.lastName.isNotBlank()) {
            getErrorMessage(person, null, "lastName cannot be blank")
        }
        require(person.siblings.map { it.parents }.toSet().size <= 1) {
            getErrorMessage(person, null, "siblings has to have same parents")
        }
        person.children.forEach {
            require(person.birthDate < it.birthDate) {
                getErrorMessage(person, it, "cannot be younger than")
            }
        }
        person.spouse?.let {
            require(it.male != person.male) {
                getErrorMessage(person, it, "cannot be the same gender as")
            }
        }
    }

    private fun getErrorMessage(person1: LinkedPerson?, person2: LinkedPerson?, text: String): String {
        val person1Text = person1?.let { dataFormatter.formatPerson(it) + " " } ?: ""
        val person2Text = person2?.let {  " " + dataFormatter.formatPerson(it) } ?: ""
        return "$person1Text$text$person2Text"
    }


}