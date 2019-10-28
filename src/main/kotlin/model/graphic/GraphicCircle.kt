package model.graphic

import javafx.scene.paint.Color
import javafx.scene.canvas.GraphicsContext
import model.constants.Constants
import model.enums.CircleAlgorithm
import model.math.Circle
import model.math.Point
import java.lang.Math.PI

class GraphicCircle : Circle, Form {
    var color: Color = Color.BLACK
    var name = ""
    var width = Constants.DEFAULT_DRAW_DIAMETER

    // region CONSTRUCTORS

    constructor(center: GraphicPoint, radius: Double, width: Int? = null, color: Color? = null, name: String? = null)
            : super(center, radius) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
    }

    constructor(xC: Int, yC: Int, radius: Double, width: Int? = null, color: Color? = null, name: String? = null)
            :super(xC, yC, radius) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
    }

    // endregion

    // region DRAW

    companion object {
        fun draw(
            g: GraphicsContext,
            xC: Int, yC: Int,
            radius: Double,
            width: Int,
            color: Color? = null,
            name: String? = null,
            algorithm: CircleAlgorithm = CircleAlgorithm.DEFAULT
        ) {
            val circle = GraphicCircle(xC, yC, radius, width, color, name)
            circle.drawCircle(g, algorithm)
        }


        fun draw(
            g: GraphicsContext,
            xC: Double, yC: Double,
            radius: Double,
            width: Int,
            color: Color? = null,
            name: String? = null,
            algorithm: CircleAlgorithm = CircleAlgorithm.DEFAULT
        ) {
            draw(g, xC.toInt(), yC.toInt(), radius, width, color, name, algorithm)
        }
    }

    fun drawCircle(g: GraphicsContext, algorithm: CircleAlgorithm = CircleAlgorithm.DEFAULT) {
        when (algorithm) {
            CircleAlgorithm.DEFAULT -> drawCircleDefault(g)
            CircleAlgorithm.POLAR -> drawCirclePolar(g)
            CircleAlgorithm.MIDPOINT -> drawCircleMidPoint(g)
            else -> drawCircleGraphicsContext(g)
        }
    }

    override fun draw(gc: GraphicsContext) {
        drawCircle(gc)
    }

    override fun normalize(min: Point, max: Point): GraphicCircle {
        val cx = (center.x - min.x) / (max.x - min.x)
        val cy = (center.y - min.y) / (max.y - min.y)
        val nRadius = (radius - min.x) / (max.x - min.x)

        return GraphicCircle(GraphicPoint(cx, cy), nRadius, color=color)
    }

    override fun convertFromNormalized(min: Point, max: Point) {
        val cx = center.x * (max.x - min.x) + min.x
        val cy = center.y * (max.y - min.y) + min.y

        radius = radius * (max.x - min.x) + min.x
        center = Point(cx, cy)
    }

    // region DRAW CIRCLE ALGORITHMS

    private fun drawCircleDefault(g: GraphicsContext) {
        val x1 = calculateX(PI / 4, radius).toInt() // 45º

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

            GraphicPoint(cXaddX, cYaddY, color, width).drawPoint(g)
            GraphicPoint(cXaddY, cYaddX, color, width).drawPoint(g)
            GraphicPoint(cXaddX, cYsubY, color, width).drawPoint(g)
            GraphicPoint(cXaddY, cYsubX, color, width).drawPoint(g)
            GraphicPoint(cXsubX, cYaddY, color, width).drawPoint(g)
            GraphicPoint(cXsubY, cYaddX, color, width).drawPoint(g)
            GraphicPoint(cXsubX, cYsubY, color, width).drawPoint(g)
            GraphicPoint(cXsubY, cYsubX, color, width).drawPoint(g)
        }


    }

    private fun drawCirclePolar(g: GraphicsContext) {
        // TODO
    }

    private fun drawCircleMidPoint(g: GraphicsContext) {
        // TODO
    }

    private fun drawCircleGraphicsContext(g: GraphicsContext) {
        // TODO
    }

    // endregion
}