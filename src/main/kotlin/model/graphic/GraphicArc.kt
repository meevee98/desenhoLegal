package model.graphic

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import model.constants.Constants
import model.math.Circle
import model.math.Point

class GraphicArc (
        center: GraphicPoint,
        radius: Double,
        val minX: Int, val minY: Int,
        val maxX: Int, val maxY: Int,
        width: Int? = null,
        color: Color? = null,
        name: String? = null
): Form, Circle(center, radius) {
    var color: Color = Color.BLACK
    var name = ""
    var width = Constants.DEFAULT_DRAW_DIAMETER

    // region CONSTRUCTORS

    init {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
    }

    constructor(center: GraphicPoint, radius: Double, min: GraphicPoint, max: GraphicPoint, width: Int? = null, color: Color? = null, name: String? = null)
            : this(center, radius, min.x.toInt(), min.y.toInt(), max.x.toInt(), max.y.toInt(), width, color, name)

    constructor(
            xC: Int, yC: Int,
            radius: Double,
            xMin: Int, yMin: Int,
            xMax: Int, yMax: Int,
            width: Int? = null,
            color: Color? = null,
            name: String? = null
    ) : this(GraphicPoint(xC, yC), radius, xMin, yMin, xMax, yMax, width, color, name)

    // endregion

    // region DRAW

    override fun draw(gc: GraphicsContext) {
        drawArc(gc)
    }

    override fun normalize(min: Point, max: Point): GraphicArc {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun convertFromNormalized(min: Point, max: Point) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun drawArc(gc: GraphicsContext) {
        val x1 = calculateX(Math.PI / 4, radius).toInt() // 45º
        val cx = center.x.toInt()
        val cy = center.y.toInt()

        for (x in 0..x1) {
            val y = calculateY(x.toDouble(), radius).toInt()

            validateCoordinatesAndDrawPoint(cx + x, cy + y, gc)
            validateCoordinatesAndDrawPoint(cx + x, cy - y, gc)
            validateCoordinatesAndDrawPoint(cx + y, cy + x, gc)
            validateCoordinatesAndDrawPoint(cx + y, cy - x, gc)
            validateCoordinatesAndDrawPoint(cx - x, cy + y, gc)
            validateCoordinatesAndDrawPoint(cx - x, cy - y, gc)
            validateCoordinatesAndDrawPoint(cx - y, cy + x, gc)
            validateCoordinatesAndDrawPoint(cx - y, cy - x, gc)
        }
    }

    private fun validateCoordinatesAndDrawPoint(x: Int, y: Int, gc: GraphicsContext) {
        if (x < minX || x > maxX) {
            return
        }
        if (y < minY || y > maxY) {
            return
        }
        GraphicPoint(x, y, color, width).drawPoint(gc)
    }

    // endregion

}
