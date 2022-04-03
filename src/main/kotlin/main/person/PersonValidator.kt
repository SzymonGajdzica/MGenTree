package main.person

import main.data.LinkedPerson

interface PersonValidator {
    fun validate(person: LinkedPerson)
}