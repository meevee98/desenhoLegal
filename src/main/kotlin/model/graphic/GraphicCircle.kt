package model.graphic

import model.enums.CircleAlgorithm
import model.math.Circle
import java.awt.Color
import java.awt.Graphics

class GraphicCircle : Circle {
    //TODO: change java.awt to TornadoFX
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
            g: Graphics,
            xC: Int, yC: Int,
            radius: Double,
            color: Color,
            name: String,
            width: Int,
            algorithm: CircleAlgorithm = CircleAlgorithm.DEFAULT
        ) {
            val circle = GraphicCircle(xC, yC, radius, width, color, name)
            circle.drawCircle(g, algorithm)
        }


        fun draw(
            g: Graphics,
            xC: Double,yC: Double,
            radius: Double,
            color: Color,
            name: String,
            width: Int,
            algorithm: CircleAlgorithm = CircleAlgorithm.DEFAULT
        ) {
            draw(g, xC.toInt(), yC.toInt(), radius, color, name, width, algorithm)
        }
    }

    fun drawCircle(g: Graphics, algorithm: CircleAlgorithm = CircleAlgorithm.DEFAULT) {
        when (algorithm) {
            CircleAlgorithm.POLAR -> drawCirclePolar(g)
            CircleAlgorithm.MIDPOINT -> drawCircleMidPoint(g)
            else -> drawCircleGraphics(g)
        }
    }

    // region DRAW CIRCLE ALGORITHMS

    private fun drawCirclePolar(g: Graphics) {
        // TODO
    }

    private fun drawCircleMidPoint(g: Graphics) {
        // TODO
    }

    private fun drawCircleGraphics(g: Graphics) {
        // TODO
    }

    // endregion
}