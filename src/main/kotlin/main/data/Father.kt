package main.data

import main.link.FamilyLinkProvider

class Father(person1: Person, person2: Person, private val generationDifference: Int): FamilyLink(person1, person2) {

    companion object {
        val provider = object: FamilyLinkProvider {
            override fun provide(person1: LinkedPerson, person2: LinkedPerson): FamilyLink? {
                return getFamilyGeneration(person1, person2, 1)?.let { Father(person1, person2, it) }
            }
            private fun getFamilyGeneration(parent: LinkedPerson, child: LinkedPerson, generationDifference: Int): Int? {
                if(parent.children.contains(child)) {
                    return generationDifference
                }
                parent.children.forEach {
                    val result = getFamilyGeneration(it, child, generationDifference + 1)
                    if(result != null)
                        return result
                }
                return null
            }

            override fun provide(label: String, person1: Person, person2: Person): FamilyLink? {
                return if(label.startsWith("Father")) Father(person1, person2, label.last().digitToInt()) else null
            }
        }
    }

    override val name: String
        get() {
            var mainName = if(person1.birthDate < person2.birthDate)
                if(person1.male) "father" else "mother"
            else
                if(person1.male) "son" else "daughter"
            if(generationDifference > 1) {
                mainName = "grand$mainName"
                repeat(generationDifference - 2) {
                    mainName = "great $mainName"
                }
            }
            return mainName
        }
    override val label: String = "Father$generationDifference"

    override fun reversed(): FamilyLink {
        return Father(person2, person1, generationDifference)
    }

}