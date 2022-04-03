package main.data

import main.link.FamilyLinkProvider

class Brother(person1: Person, person2: Person): FamilyLink(person1, person2) {

    companion object {
        val provider = object: FamilyLinkProvider {
            override fun provide(person1: LinkedPerson, person2: LinkedPerson): FamilyLink? {
                return if(person1.siblings.contains(person2)) Brother(person1, person2) else null
            }

            override fun provide(label: String, person1: Person, person2: Person): FamilyLink? {
                return if(label == "Brother") Brother(person1, person2) else null
            }
        }
    }

    override val name: String = if(person1.male) "brother" else "sister"

    override val label: String = "Brother"

    override fun reversed(): FamilyLink {
        return Brother(person2, person1)
    }

}