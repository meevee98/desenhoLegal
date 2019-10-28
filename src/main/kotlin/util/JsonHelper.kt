package util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import javafx.scene.paint.Color
import model.graphic.*
import model.math.Point

class JsonHelper {
    fun saveFigureAsJson(forms: List<Form>, min: Point, max: Point) {
        val normalizedForms = forms.map { it.normalize(min, max) }
        val lines = normalizedForms.filterIsInstance<GraphicLine>()
        val circles = normalizedForms.filterIsInstance<GraphicCircle>()
        val rectangle = normalizedForms.filterIsInstance<GraphicRectangle>()

        val mapper = ObjectMapper()

        val figureNode = mapper.createObjectNode()
        figureNode.set("reta", contructLineNode(lines))
        figureNode.set("circulo", constructCircleNode(circles))
        figureNode.set("retangulo", constructRectangleNode(rectangle))

        val root = mapper.createObjectNode()
        root.set("figura", figureNode)

        println(mapper.writeValueAsString(root))
    }

    fun readFigureFromJson(min: Point, max: Point): List<Form> {
        val json = "{\"figura\":{\"reta\":[{\"p1\":{\"x\":0.18277945619335348,\"y\":0.2764350453172205},\"p2\":{\"x\":0.4486404833836858,\"y\":0.5015105740181269},\"cor\":{\"r\":0,\"g\":0,\"b\":0}},{\"p1\":{\"x\":0.18882175226586104,\"y\":0.7643504531722054},\"p2\":{\"x\":0.4743202416918429,\"y\":0.16163141993957703},\"cor\":{\"r\":0,\"g\":0,\"b\":0}}],\"circulo\":[{\"ponto\":{\"x\":0.7447129909365559,\"y\":0.49395770392749244},\"raio\":0.37108811874408015,\"cor\":{\"r\":0,\"g\":0,\"b\":0}}],\"retangulo\":[{\"p1\":{\"x\":0.012084592145015106,\"y\":0.17673716012084592},\"p2\":{\"x\":0.3323262839879154,\"y\":0.6586102719033232},\"cor\":{\"r\":0,\"g\":0,\"b\":0}}]}}"
        val forms = mutableListOf<Form>()

        val mapper = ObjectMapper()
        val tree = mapper.readTree(json)

        tree?.let { root ->
            root["figura"]?.let { figure ->
                figure["reta"]?.run {
                    val lines = readLineNode(this)
                    forms.addAll(lines)
                }
                figure["circulo"]?.run {
                    val circles = readCircleNode(this)
                    forms.addAll(circles)
                }
                figure["retangulo"]?.run {
                    val rectangles = readRectangleNode(this)
                    forms.addAll(rectangles)
                }
            }
        }

        forms.forEach { it.convertFromNormalized(min, max) }
        return forms
    }

    private fun contructLineNode(lines: List<GraphicLine>): JsonNode{
        val mapper = ObjectMapper()
        val linesNode = mapper.createArrayNode()

        for (line in lines) {
            val p1 = mapper.createObjectNode()
            p1.put("x", line.p1.x)
            p1.put("y", line.p1.y)

            val p2 = mapper.createObjectNode()
            p2.put("x", line.p2.x)
            p2.put("y", line.p2.y)

            val color = mapper.createObjectNode()
            color.put("r", (255 * line.color.red).toInt())
            color.put("g", (255 * line.color.green).toInt())
            color.put("b", (255 * line.color.blue).toInt())

            val lineNode = mapper.createObjectNode()
            lineNode.set("p1", p1)
            lineNode.set("p2", p2)
            lineNode.set("cor", color)

            linesNode.add(lineNode)
        }

        return linesNode
    }

    private fun constructCircleNode(circles: List<GraphicCircle>): JsonNode {
        val mapper = ObjectMapper()
        val circlesNode = mapper.createArrayNode()

        for (circle in circles) {
            // Objeto ponto
            val point = mapper.createObjectNode()
            point.put("x", circle.center.x)
            point.put("y", circle.center.y)

            // Objeto cor
            val color = mapper.createObjectNode()
            color.put("r", (255 * circle.color.red).toInt())
            color.put("g", (255 * circle.color.green).toInt())
            color.put("b", (255 * circle.color.blue).toInt())

            // Objeto círculo
            val circleNode = mapper.createObjectNode()
            circleNode.set("ponto", point)
            circleNode.put("raio", circle.radius)
            circleNode.set("cor", color)

            circlesNode.add(circleNode)
        }

        return circlesNode
    }

    private fun readLineNode(linesNode: JsonNode): List<Form> {
        val lines = mutableListOf<GraphicLine>()

        for (node in linesNode) {
            val p1 = node["p1"]?.let {
                val x = it["x"]?.asDouble() ?: 0.0
                val y = it["y"]?.asDouble() ?: 0.0
                GraphicPoint(x, y)
            }

            val p2 = node["p2"]?.let {
                val x = it["x"]?.asDouble() ?: 0.0
                val y = it["y"]?.asDouble() ?: 0.0
                GraphicPoint(x, y)
            }

            val color = node["cor"]?.let {
                val red = (it["r"]?.asInt() ?: 0) / 255.0
                val green = (it["g"]?.asInt() ?: 0) / 255.0
                val blue = (it["b"]?.asInt() ?: 0) / 255.0
                Color(red, green, blue, 1.0)
            }

            p1?.also { p ->
                p2?.also {
                    lines.add(GraphicLine(p1, p2, color=color))
                }
            }
        }

        return lines
    }

    private fun readCircleNode(circlesNode: JsonNode): List<Form> {
        val circles = mutableListOf<GraphicCircle>()

        for (node in circlesNode) {
            val center = node["ponto"]?.let {
                val x = it["x"]?.asDouble() ?: 0.0
                val y = it["y"]?.asDouble() ?: 0.0
                GraphicPoint(x, y)
            }

            val radius = node["raio"]?.asDouble() ?: 0.0

            val color = node["cor"]?.let {
                val red = (it["r"]?.asInt() ?: 0) / 255.0
                val green = (it["g"]?.asInt() ?: 0) / 255.0
                val blue = (it["b"]?.asInt() ?: 0) / 255.0
                Color(red, green, blue, 1.0)
            }

            center?.also {
                circles.add(GraphicCircle(it, radius, color=color))
            }
        }

        return circles
    }

    private fun readRectangleNode(rectanglesNode: JsonNode): List<Form> {
        val rectangles = mutableListOf<GraphicRectangle>()

        for (node in rectanglesNode) {
            val p1 = node["p1"]?.let {
                val x = it["x"]?.asDouble() ?: 0.0
                val y = it["y"]?.asDouble() ?: 0.0
                GraphicPoint(x, y)
            }

            val p2 = node["p2"]?.let {
                val x = it["x"]?.asDouble() ?: 0.0
                val y = it["y"]?.asDouble() ?: 0.0
                GraphicPoint(x, y)
            }

            val color = node["cor"]?.let {
                val red = (it["r"]?.asInt() ?: 0) / 255.0
                val green = (it["g"]?.asInt() ?: 0) / 255.0
                val blue = (it["b"]?.asInt() ?: 0) / 255.0
                Color(red, green, blue, 1.0)
            }

            p1?.also { p ->
                p2?.also {
                    rectangles.add(GraphicRectangle(p1, p2, color=color))
                }
            }
        }

        return rectangles
    }

    private fun constructRectangleNode(rectangles: List<GraphicRectangle>): JsonNode {
        val mapper = ObjectMapper()
        val rectanglesNode = mapper.createArrayNode()

        for (rectangle in rectangles){
            val p1 = mapper.createObjectNode()
            p1.put("x", rectangle.p1.x)
            p1.put("y", rectangle.p1.y)

            val p2 = mapper.createObjectNode()
            p2.put("x", rectangle.p2.x)
            p2.put("y", rectangle.p2.y)

            val color = mapper.createObjectNode()
            color.put("r", (255 * rectangle.color.red).toInt())
            color.put("g", (255 * rectangle.color.green).toInt())
            color.put("b", (255 * rectangle.color.blue).toInt())

            val rectangleNode = mapper.createObjectNode()
            rectangleNode.set("p1", p1)
            rectangleNode.set("p2", p2)
            rectangleNode.set("cor", color)

            rectanglesNode.add(rectangleNode)
        }

        return rectanglesNode
    }
}