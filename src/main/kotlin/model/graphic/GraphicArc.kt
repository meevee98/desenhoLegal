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

    // region DRAW

    override fun draw(gc: GraphicsContext) {
        drawArc(gc)
    }

    fun drawArc(gc: GraphicsContext) {
        val x1 = calculateX(Math.PI / 4, radius).toInt() // 45º

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

            validateCoordinatesAndDrawPoint(cXaddX, cYaddY, gc)
            validateCoordinatesAndDrawPoint(cXaddX, cYsubY, gc)
            validateCoordinatesAndDrawPoint(cXaddY, cYaddX, gc)
            validateCoordinatesAndDrawPoint(cXaddY, cYsubX, gc)
            validateCoordinatesAndDrawPoint(cXsubX, cYaddY, gc)
            validateCoordinatesAndDrawPoint(cXsubX, cYsubY, gc)
            validateCoordinatesAndDrawPoint(cXsubY, cYaddX, gc)
            validateCoordinatesAndDrawPoint(cXsubY, cYsubX, gc)
        }
    }

    private fun validateCoordinatesAndDrawPoint(x: Int, y: Int, gc: GraphicsContext) {
        if (x < min.x || x > max.x) {
            return
        }
        if (y < min.y || y > max.x) {
            return
        }
        GraphicPoint(x, y, color, width).drawPoint(gc)
    }

    // endregion

}
