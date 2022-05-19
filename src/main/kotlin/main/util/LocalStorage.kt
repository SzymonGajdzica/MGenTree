package main.util

interface LocalStorage {
    fun loadRawPersonList(filepath: String): List<String>

}