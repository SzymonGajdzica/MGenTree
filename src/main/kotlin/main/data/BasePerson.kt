package main.data

import java.time.Instant

data class BasePerson(
    override val id: String,
    override val firstName: String,
    override val lastName: String,
    override val birthDate: Instant,
    override val male: Boolean
): Person