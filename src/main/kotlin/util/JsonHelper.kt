package util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import javafx.scene.paint.Color
import model.graphic.*
import model.math.Point
import java.io.File

class JsonHelper {
    fun saveFigureAsJson(forms: List<Form>, file: File, min: Point, max: Point) {
        val normalizedForms = forms.map { it.normalize(min, max) }
        val lines = normalizedForms.filterIsInstance<GraphicLine>()
        val circles = normalizedForms.filterIsInstance<GraphicCircle>()
        val rectangles = normalizedForms.filterIsInstance<GraphicRectangle>()
        val polygons = normalizedForms.filterIsInstance<GraphicPolygon>()

        val mapper = ObjectMapper()

        val figureNode = mapper.createObjectNode()
        figureNode.set("reta", contructLineNode(lines))
        figureNode.set("circulo", constructCircleNode(circles))
        figureNode.set("retangulo", constructRectangleNode(rectangles))
        figureNode.set("poligono", constructPolygonNode(polygons))

        val root = mapper.createObjectNode()
        root.set("figura", figureNode)

        println(mapper.writeValueAsString(root))
        mapper.writer().writeValue(file, root)
    }

    fun readFigureFromJson(file: File, min: Point, max: Point): List<Form> {
        val forms = mutableListOf<Form>()

        val mapper = ObjectMapper()
        val tree = mapper.readTree(file)

        tree?.let { root ->
            root["figura"]?.also { figure ->
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
                figure["poligono"]?.run {
                    val polygons = readPolygonNode(this)
                    forms.addAll(polygons)
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

    private fun constructRectangleNode(rectangles: List<GraphicRectangle>): JsonNode {
        val mapper = ObjectMapper()
        val rectanglesNode = mapper.createArrayNode()

        for (rectangle in rectangles) {
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

    private fun constructPolygonNode(polygons: List<GraphicPolygon>): JsonNode {
        val mapper = ObjectMapper()
        val polygonsNode = mapper.createArrayNode()

        for (polygon in polygons) {
            val points = mapper.createArrayNode()
            for (point in polygon.points) {
                val p = mapper.createObjectNode()
                p.put("x", point.x)
                p.put("y", point.y)

                points.add(p)
            }

            val color = mapper.createObjectNode()
            color.put("r", (255 * polygon.color.red).toInt())
            color.put("g", (255 * polygon.color.green).toInt())
            color.put("b", (255 * polygon.color.blue).toInt())

            val polygonNode = mapper.createObjectNode()
            polygonNode.set("ponto", points)
            polygonNode.set("cor", color)

            polygonsNode.add(polygonNode)
        }

        return polygonsNode
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

    private fun readPolygonNode(polygonsNode: JsonNode): List<Form> {
        val polygons = mutableListOf<GraphicPolygon>()

        for (node in polygonsNode) {
            val points = mutableListOf<GraphicPoint>()
            node["ponto"]?.let { pointsNode ->
                for (point in pointsNode) {
                    val x = point["x"]?.asDouble() ?: 0.0
                    val y = point["y"]?.asDouble() ?: 0.0
                    points.add(GraphicPoint(x, y))
                }
            }

            val color = node["cor"]?.let {
                val red = (it["r"]?.asInt() ?: 0) / 255.0
                val green = (it["g"]?.asInt() ?: 0) / 255.0
                val blue = (it["b"]?.asInt() ?: 0) / 255.0
                Color(red, green, blue, 1.0)
            }

            if (points.size > 2) {
                polygons.add(GraphicPolygon(points, color=color))
            }
        }

        return polygons
    }
}