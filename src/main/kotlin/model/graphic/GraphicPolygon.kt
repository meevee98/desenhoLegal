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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun convertFromNormalized(min: Point, max: Point) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}