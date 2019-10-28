package model.graphic

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import model.constants.Constants
import model.math.Point

class GraphicPolygon: Form {
    var color: Color = Color.BLACK
    var name = ""
    var width = Constants.DEFAULT_DRAW_DIAMETER
    var points: MutableList<GraphicPoint> = mutableListOf()

    constructor(width: Int? = null, color: Color? = null, name: String? = null) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
    }

    constructor(points: List<GraphicPoint>, width: Int? = null, color: Color? = null, name: String? = null)
            : this(width, color, name) {
        this.points = points.toMutableList()
    }

    fun addPoint(x: Double, y: Double, gc: GraphicsContext) {
        val p1 = GraphicPoint(x, y, color, width)

        if(points.isNotEmpty()) {
            val p2 = points.last()
            GraphicLine(p1, p2, width, color).draw(gc)
        }
        else {
            p1.draw(gc)
        }

        points.add(p1)
    }

    override fun draw(gc: GraphicsContext) {
        val lastIndex = points.lastIndex
        points.forEachIndexed { index, p1 ->
            var p2: GraphicPoint
            if (index == lastIndex) {
                p2 = points.first()
            }
            else {
                p2 = points[index + 1]
            }

            GraphicLine(p1, p2, width, color).draw(gc)
        }
    }

    override fun normalize(min: Point, max: Point): GraphicPolygon {
        val normalizedPoints = mutableListOf<GraphicPoint>()
        points.forEach {
            val x = (it.x - min.x) / (max.x - min.x)
            val y = (it.y - min.y) / (max.y - min.y)

            normalizedPoints.add(GraphicPoint(x, y))
        }

        return GraphicPolygon(normalizedPoints, color=color)
    }

    override fun convertFromNormalized(min: Point, max: Point) {
        val pts = mutableListOf<GraphicPoint>()

        points.forEach {
            val x = it.x * (max.x - min.x) + min.x
            val y = it.y * (max.y - min.y) + min.y

            pts.add(GraphicPoint(x, y))
        }
        points.clear()
        points.addAll(pts)
    }
}