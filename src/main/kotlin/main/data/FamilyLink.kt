package main.data

abstract class FamilyLink(
    val person1: Person,
    val person2: Person,
) {
    abstract val name: String
    abstract val label: String
    abstract fun reversed(): FamilyLink
}