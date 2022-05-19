package main.util

import java.io.File

class LocalStorageImpl : LocalStorage {

    override fun loadRawPersonList(filepath: String): List<String> {
        return File(filepath).readLines().filter { it.isNotBlank() }
    }

}