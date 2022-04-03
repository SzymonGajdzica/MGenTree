package main.data

import main.link.FamilyLinkProvider

class BrotherInLaw(person1: Person, person2: Person): FamilyLink(person1, person2) {

    companion object {
        val provider = object: FamilyLinkProvider {
            override fun provide(person1: LinkedPerson, person2: LinkedPerson): FamilyLink? {
                return if(person1.spouse?.siblings?.contains(person2) == true) BrotherInLaw(person1, person2) else null
            }
            override fun provide(label: String, person1: Person, person2: Person): FamilyLink? {
                return if(label == "BrotherInLaw") BrotherInLaw(person1, person2) else null
            }
        }
    }

    override val name: String = if(person1.male) "brother-in-law" else "sister-in-law"
    override val label: String = "BrotherInLaw"

    override fun reversed(): FamilyLink {
        return BrotherInLaw(person2, person1)
    }


}