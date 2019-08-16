package model.graphic

import model.enums.LineAlgorithm
import model.math.Line
import java.awt.Color
import java.awt.Graphics
import kotlin.math.abs

class GraphicLine : Line {
    //TODO: change java.awt to TornadoFX
    var color: Color = Color.BLACK
    var name = ""
    var width = 1

    // region CONSTRUCTORS

    constructor(p1: GraphicPoint, p2: GraphicPoint, width: Int? = null, color: Color? = null, name: String? = null)
            : super(p1, p2) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
    }

    constructor(x1: Int, y1: Int, x2: Int, y2: Int, width: Int? = null, color: Color? = null, name: String? = null)
            :super(x1, y1, x2, y2) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
    }
    
    // endregion

    // region DRAW

    companion object {
        fun draw(
            g: Graphics,
            x1: Int, y1: Int,
            x2: Int, y2: Int,
            color: Color,
            name: String,
            width: Int,
            algorithm: LineAlgorithm = LineAlgorithm.DEFAULT
        ) {
            val line = GraphicLine(x1, y1, x2, y2, width, color, name)
            line.drawLine(g, algorithm)
        }

        fun draw(
            g: Graphics,
            x1: Double, y1: Double,
            x2: Double, y2: Double,
            color: Color,
            name: String,
            width: Int,
            algorithm: LineAlgorithm = LineAlgorithm.DEFAULT
        ) {
            draw(g, x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt(), color, name, width, algorithm)
        }
    }

    fun drawLine(g: Graphics, alg: LineAlgorithm = LineAlgorithm.DEFAULT) {
        when (alg) {
            LineAlgorithm.EQUATION -> drawLineEquation(g)
            LineAlgorithm.DDA -> drawLineDDA(g)
            LineAlgorithm.MIDPOINT -> drawLineMidPoint(g)
            else -> drawLineGraphics(g)
        }
    }

    // region DRAW LINE ALGORITHMS

    private fun drawLineEquation(g: Graphics) {
        // TODO
        val b = calculateYInterception()
        val m = calculateSlope()

        val deltaX = abs(p2.x - p1.x)
        val deltaY = abs(p2.y - p1.y)

//        x1 = x2 // deltaX = 0
//        y1 = y2 // deltaY = 0
//        deltaX > deltaY
//        deltaX <= deltaY
    }

    private fun drawLineDDA(g: Graphics) {
        // TODO
    }

    private fun drawLineMidPoint(g: Graphics) {
        // TODO
    }

    private fun drawLineGraphics(g: Graphics) {
        // TODO
    }
    
    // endregion
}