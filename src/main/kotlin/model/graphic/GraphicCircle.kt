package model.graphic

import javafx.scene.paint.Color
import javafx.scene.canvas.GraphicsContext
import model.enums.CircleAlgorithm
import model.math.Circle
import java.lang.Math.PI

class GraphicCircle : Circle, Form {
    //TODO: change javafx to TornadoFX
    var color: Color = Color.BLACK
    var name = ""
    var width = 1

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

    // region DRAW CIRCLE ALGORITHMS

    private fun drawCircleDefault(g: GraphicsContext) {
        val x1 = calculateX(PI / 4, radius).toInt() // 45º

        for (x in 0..x1) {
            val y = calculateY(x.toDouble(), radius)

            GraphicPoint(center.x + x, center.y + y, color, width).drawPoint(g)
            GraphicPoint(center.x + y, center.y + x, color, width).drawPoint(g)
            GraphicPoint(center.x + x, center.y - y, color, width).drawPoint(g)
            GraphicPoint(center.x + y, center.y - x, color, width).drawPoint(g)
            GraphicPoint(center.x - x, center.y + y, color, width).drawPoint(g)
            GraphicPoint(center.x - y, center.y + x, color, width).drawPoint(g)
            GraphicPoint(center.x - x, center.y - y, color, width).drawPoint(g)
            GraphicPoint(center.x - y, center.y - x, color, width).drawPoint(g)
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