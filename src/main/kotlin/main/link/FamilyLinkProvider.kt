package main.link

import main.data.FamilyLink
import main.data.LinkedPerson
import main.data.Person

interface FamilyLinkProvider {
    fun provide(person1: LinkedPerson, person2: LinkedPerson): FamilyLink?
    fun provide(label: String, person1: Person, person2: Person): FamilyLink?
}