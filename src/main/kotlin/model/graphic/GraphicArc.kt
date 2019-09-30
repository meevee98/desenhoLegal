package model.graphic

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import model.math.Circle

class GraphicArc(
        center: GraphicPoint,
        radius: Double,
        val min: GraphicPoint,
        val max: GraphicPoint,
        width: Int? = null,
        color: Color? = null,
        name: String? = null
): Form, Circle(center, radius) {
    var color: Color = Color.BLACK
    var name = ""
    var width = 1

    // region CONSTRUCTORS

    init {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
    }

    constructor(
            xC: Int, yC: Int,
            radius: Double,
            xMin: Int, yMin: Int,
            xMax: Int, yMax: Int,
            width: Int? = null,
            color: Color? = null,
            name: String? = null
    ) : this(GraphicPoint(xC, yC), radius, GraphicPoint(xMin, yMin), GraphicPoint(xMax, yMax)) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
    }

    // endregion
    override fun draw(gc: GraphicsContext) {
        val x1 = calculateX(Math.PI / 4, radius).toInt() // 45º
        val minX = min.x.toInt()
        val minY = min.y.toInt()
        val maxX = max.x.toInt()
        val maxY = max.y.toInt()

        for (x in 0..x1) {
            val y = calculateY(x.toDouble(), radius)

            val cXaddX = (center.x + x).toInt()
            val cXaddY = (center.x + y).toInt()
            val cYaddX = (center.y + x).toInt()
            val cYaddY = (center.y + y).toInt()
            val cXsubX = (center.x - x).toInt()
            val cXsubY = (center.x - y).toInt()
            val cYsubX = (center.y - x).toInt()
            val cYsubY = (center.y - y).toInt()

            if (cXaddX in minX..maxX) {
                if (cYaddY in minY..maxY) {
                    GraphicPoint(cXaddX, cYaddY, color, width).drawPoint(gc)
                }
                if (cYsubY in minY..maxY) {
                    GraphicPoint(cXaddX, cYsubY, color, width).drawPoint(gc)
                }
            }
            if (cXaddY in minX..maxX) {
                if (cYaddX in minY..maxY) {
                    GraphicPoint(cXaddY, cYaddX, color, width).drawPoint(gc)
                }
                if (cYsubX in minY..maxY) {
                    GraphicPoint(cXaddY, cYsubX, color, width).drawPoint(gc)
                }
            }
            if (cXsubX in minX..maxX) {
                if (cYaddY in minY..maxY) {
                    GraphicPoint(cXsubX, cYaddY, color, width).drawPoint(gc)
                }
                if (cYsubY in minY..maxY) {
                    GraphicPoint(cXsubX, cYsubY, color, width).drawPoint(gc)
                }
            }
            if (cXsubY in minX..maxX) {
                if (cYaddX in minY..maxY) {
                    GraphicPoint(cXsubY, cYaddX, color, width).drawPoint(gc)
                }
                if (cYsubX in minY..maxY) {
                    GraphicPoint(cXsubY, cYsubX, color, width).drawPoint(gc)
                }
            }

        }


    }
}
