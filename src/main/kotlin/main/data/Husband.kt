package main.data

import main.link.FamilyLinkProvider

class Husband(person1: Person, person2: Person): FamilyLink(person1, person2) {

    companion object {
        val provider = object: FamilyLinkProvider {
            override fun provide(person1: LinkedPerson, person2: LinkedPerson): FamilyLink? {
                return if(person1.spouse == person2) Husband(person1, person2) else null
            }
            override fun provide(label: String, person1: Person, person2: Person): FamilyLink? {
                return if(label == "Husband") Husband(person1, person2) else null
            }
        }
    }

    override val name: String = if(person1.male) "husband" else "wife"
    override val label: String = "Husband"

    override fun reversed(): FamilyLink {
        return Husband(person2, person1)
    }

}