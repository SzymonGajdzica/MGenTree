package main.data

import main.link.FamilyLinkProvider

class Uncle(person1: Person, person2: Person): FamilyLink(person1, person2) {

    companion object {
        val provider = object: FamilyLinkProvider {
            override fun provide(person1: LinkedPerson, person2: LinkedPerson): FamilyLink? {
                return if(person1.siblings.any { it.children.contains(person2) })
                    Uncle(person1, person2)
                else
                    null
            }
            override fun provide(label: String, person1: Person, person2: Person): FamilyLink? {
                return if(label == "Uncle") Uncle(person1, person2) else null
            }
        }
    }

    override val name: String
        get() {
            return if(person1.birthDate < person2.birthDate)
                if(person1.male) "uncle" else "aunt"
            else
                if(person1.male) "nephew" else "niece"
        }
    override val label: String = "Uncle"


    override fun reversed(): FamilyLink {
        return Uncle(person2, person1)
    }

}