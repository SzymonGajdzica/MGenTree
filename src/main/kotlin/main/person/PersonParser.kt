package main.person

import main.data.LinkedPerson

interface PersonParser {
    fun parseRawPersonList(rawPersonList: List<String>): List<LinkedPerson>
}