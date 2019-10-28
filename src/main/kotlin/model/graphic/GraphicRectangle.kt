package model.graphic

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import model.constants.Constants
import model.math.Point
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class GraphicRectangle: Form {
    var color: Color = Color.BLACK
    var name = ""
    var width = Constants.DEFAULT_DRAW_DIAMETER
    var p1: GraphicPoint
    var p2: GraphicPoint

    constructor(p1: GraphicPoint, p2: GraphicPoint, width: Int? = null, color: Color? = null, name: String? = null) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
        this.p1 = p1
        this.p2 = p2
    }


    constructor(x1: Int, y1: Int, x2: Int, y2: Int, width: Int? = null, color: Color? = null, name: String? = null) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
        this.p1 = GraphicPoint(x1.toDouble(), y1.toDouble())
        this.p2 = GraphicPoint(x2.toDouble(), y2.toDouble())
    }

    private fun calculateLines(): List<GraphicLine> {
        return listOf(
                GraphicLine(p1.x, p1.y, p1.x, p2.y, width, color, name),
                GraphicLine(p2.x, p2.y, p1.x, p2.y, width, color, name),
                GraphicLine(p2.x, p2.y, p2.x, p1.y, width, color, name),
                GraphicLine(p1.x, p1.y, p2.x, p1.y, width, color, name)
        )
    }


    override fun draw(gc: GraphicsContext) {
        calculateLines().forEach { line ->
            line.draw(gc)
        }
    }

    override fun normalize(min: Point, max: Point): GraphicRectangle {
        val x1 = (p1.x - min.x) / (max.x - min.x)
        val y1 = (p1.y - min.y) / (max.y - min.y)
        val x2 = (p2.x - min.x) / (max.x - min.x)
        val y2 = (p2.y - min.y) / (max.y - min.y)

        return GraphicRectangle(GraphicPoint(x1, y1), GraphicPoint(x2, y2), color=color)
    }

    override fun convertFromNormalized(min: Point, max: Point) {
        val x1 = p1.x * (max.x - min.x) + min.x
        val y1 = p1.y * (max.y - min.y) + min.y
        val x2 = p2.x * (max.x - min.x) + min.x
        val y2 = p2.y * (max.y - min.y) + min.y

        p1 = GraphicPoint(x1, y1)
        p2 = GraphicPoint(x2, y2)
    }

    fun getMinPoint(): Point {
        val minX = min(p1.x, p2.x)
        val minY = min(p1.y, p2.y)

        return Point(minX, minY)
    }

    fun getMaxPoint(): Point {
        val maxX = max(p1.x, p2.x)
        val maxY = max(p1.y, p2.y)

        return Point(maxX, maxY)
    }

    fun getWidth() : Double {
        return abs(p2.x - p1.x)
    }

    fun getHeight() : Double {
        return abs(p2.y - p1.y)
    }
}
