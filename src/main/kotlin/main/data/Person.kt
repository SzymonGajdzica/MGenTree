package main.data

import kotlinx.datetime.Instant

interface Person {
    val id: String
    val firstName: String
    val lastName: String
    val birthDate: Instant
    val male: Boolean
}