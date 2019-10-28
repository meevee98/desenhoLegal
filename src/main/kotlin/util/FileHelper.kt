package util

import java.io.File

class FileHelper {
    companion object {
        val JSON_EXTENSION = "json"
        val XML_EXTENSION = "xml"

        val extensions = mapOf(
                "JSON file" to JSON_EXTENSION // suporte para outras extensões de arquivo
                //, "XML file" to XML_EXTENSION
        )
    }

    fun readFile(file: File) {
        if (file.exists() && file.isFile) {
            val lastIndexOf = file.name.lastIndexOf(".")
            val fileExtention = file.name.substring(lastIndexOf + 1)

            when (fileExtention) {
                JSON_EXTENSION -> readJsonFile(file)
                XML_EXTENSION -> readXmlFile(file)
            }
        }
    }

    fun writeFile(file: File) {
        if (!file.exists()) {
            file.createNewFile()
        }

        val lastIndexOf = file.name.lastIndexOf(".")
        val fileExtention = file.name.substring(lastIndexOf + 1)

        when (fileExtention) {
            JSON_EXTENSION -> saveJsonFile(file)
            XML_EXTENSION -> saveXmlFile(file)
        }
    }

    private fun readJsonFile(file: File) {
        if (file.exists() && file.isFile && file.name.endsWith(JSON_EXTENSION)) {
            // TODO: chamar readFromJson no JsonHelper
            throw NoSuchMethodException()
        }
    }

    private fun readXmlFile(file: File) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun saveJsonFile(file: File) {
        if (file.exists() && file.isFile && file.name.endsWith(JSON_EXTENSION)) {
            // TODO: chamar writeOnJson no JsonHelper
            throw NoSuchMethodException()
        }
    }

    private fun saveXmlFile(file: File) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}