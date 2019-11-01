package util

import controller.FormStorage
import model.math.Point
import java.io.File

class FileHelper {
    companion object {
        val JSON_EXTENSION = "json"
        val XML_EXTENSION = "xml"

        val extensions = mapOf(
                "JSON file" to JSON_EXTENSION // suporte para outras extensões de arquivo
//                , "XML file" to XML_EXTENSION
        )
    }

    fun readFile(file: File, min: Point, max: Point) {
        if (file.exists() && file.isFile) {
            val lastIndexOf = file.name.lastIndexOf(".")
            val fileExtention = file.name.substring(lastIndexOf + 1)

            when (fileExtention) {
                JSON_EXTENSION -> readJsonFile(file, min, max)
                XML_EXTENSION -> readXmlFile(file, min, max)
            }
        }
    }

    fun writeFile(file: File, min: Point, max: Point) {
        if (!file.exists()) {
            file.createNewFile()
        }

        val lastIndexOf = file.name.lastIndexOf(".")
        val fileExtention = file.name.substring(lastIndexOf + 1)

        when (fileExtention) {
            JSON_EXTENSION -> saveJsonFile(file, min, max)
            XML_EXTENSION -> saveXmlFile(file, min, max)
        }
    }

    private fun readJsonFile(file: File, min: Point, max: Point) {
        if (file.exists() && file.isFile && file.name.endsWith(JSON_EXTENSION)) {
            FormStorage.read(JsonHelper().readFigureFromJson(file, min, max))
        }
    }

    private fun readXmlFile(file: File, min: Point, max: Point) {
        throw NoSuchMethodException()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun saveJsonFile(file: File, min: Point, max: Point) {
        if (file.exists() && file.isFile && file.name.endsWith(JSON_EXTENSION)) {
            JsonHelper().saveFigureAsJson(FormStorage.forms, file, min, max)
        }
    }

    private fun saveXmlFile(file: File, min: Point, max: Point) {
        throw NoSuchMethodException()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}