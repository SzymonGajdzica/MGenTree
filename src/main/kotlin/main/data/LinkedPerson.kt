package main.data

import kotlinx.datetime.Instant

class LinkedPerson private constructor(
    override val id: String,
    override val firstName: String,
    override val lastName: String,
    override val birthDate: Instant,
    override val male: Boolean,
    private val mChildren: HashSet<LinkedPerson>,
    private var mSpouse: LinkedPerson?,
    private var parent1: LinkedPerson?,
    private var parent2: LinkedPerson?,
): Person {

    val children: Set<LinkedPerson>
        get() = mChildren
    val spouse: LinkedPerson?
        get() = mSpouse
    val parents: Pair<LinkedPerson, LinkedPerson>? by lazy {
        val parent1 = parent1
        if(parent1 != null)
            return@lazy parent1 to requireNotNull(parent2)
        else {
            require(parent2 == null)
            return@lazy null
        }
    }

    val siblings: Set<LinkedPerson>
        get() = parents?.first?.children?.filter { it != this }?.toSet() ?: setOf()

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return this.id == (other as? Person)?.id
    }

    override fun toString(): String {
        return firstName
    }

    class Builder(
        private val id: String,
        private val firstName: String,
        private val lastName: String,
        private val birthDate: Instant,
        private val male: Boolean,
    ) {

        companion object {
            fun buildAll(builders: Collection<Builder>): List<LinkedPerson> {
                while (true) {
                    val notBuilt = builders.filter { !it.isFullyBuild }
                    if (notBuilt.isEmpty()) {
                        return builders.map { requireNotNull(it.build()) }.sortedBy { it.birthDate }
                    }
                    notBuilt.forEach { it.build() }
                }
            }
        }

        private var spouse: Builder? = null
        private val children: HashSet<Builder> = hashSetOf()
        private var parent1: Builder? = null
        private var parent2: Builder? = null
        private var building: Boolean = false
        var buildPerson: LinkedPerson? = null

        fun setSpouse(spouse: Builder) {
            this.spouse = spouse
            spouse.spouse = this
        }

        fun addChild(child: Builder) {
            child.parent1 = this
            child.parent2 = spouse
            children.add(child)
            spouse?.children?.add(child)
        }

        val isFullyBuild: Boolean
            get() = buildPerson?.let {
                children.size == it.mChildren.size &&
                it.mSpouse != null || spouse == null &&
                        it.parent1 != null || parent1 == null &&
                        it.parent2 != null || parent2 == null
            } == true

        fun build(): LinkedPerson? {
            buildPerson?.let { person ->
                if(!building) {
                    building = true
                    person.mChildren.addAll(children.mapNotNull { it.build() })
                    if(person.mSpouse == null)
                        person.mSpouse = spouse?.build()
                    if(person.parent1 == null)
                        person.parent1 = parent1?.build()
                    if(person.parent2 == null)
                        person.parent2 = parent2?.build()
                    building = false
                }
                return person
            }
            if(building) {
                return null
            }
            building = true
            val person = LinkedPerson(
                id = id,
                firstName = firstName,
                lastName = lastName,
                birthDate = birthDate,
                male = male,
                mChildren = HashSet(children.mapNotNull { it.build() }),
                mSpouse = spouse?.build(),
                parent1 = parent1?.build(),
                parent2 = parent2?.build(),
            )
            buildPerson = person
            building = false
            return person
        }

        override fun toString(): String {
            return "Builder $firstName $lastName"
        }

    }

}