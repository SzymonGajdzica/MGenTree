package main.data

import java.time.Instant

interface Person {
    val id: String
    val firstName: String
    val lastName: String
    val birthDate: Instant
    val male: Boolean
}