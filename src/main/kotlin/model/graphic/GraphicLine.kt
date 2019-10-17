package model.graphic

import javafx.scene.paint.Color
import javafx.scene.canvas.GraphicsContext
import model.enums.Clipping
import kotlin.math.*
import model.enums.LineAlgorithm
import model.math.Line
import model.math.Point

class GraphicLine : Line, Form {
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

    constructor(x1: Double, y1: Double, x2: Double, y2: Double, width: Int? = null, color: Color? = null, name: String? = null)
            :super(x1, y1, x2, y2) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
    }
    
    // endregion

    // region DRAW

    companion object {
        fun draw(
            g: GraphicsContext,
            x1: Int, y1: Int,
            x2: Int, y2: Int,
            width: Int,
            color: Color? = null,
            name: String? = null,
            algorithm: LineAlgorithm = LineAlgorithm.EQUATION
        ) {
            val line = GraphicLine(x1.toDouble(), y1.toDouble(), x2.toDouble(), y2.toDouble(), width, color, name)
            line.drawLine(g, algorithm)
        }

        fun draw(
            g: GraphicsContext,
            x1: Double, y1: Double,
            x2: Double, y2: Double,
            width: Int,
            color: Color? = null,
            name: String? = null,
            algorithm: LineAlgorithm = LineAlgorithm.EQUATION
        ) {
            draw(g, x1, y1, x2, y2, width, color, name, algorithm)
        }
    }

    fun drawLine(g: GraphicsContext, alg: LineAlgorithm = LineAlgorithm.EQUATION) {
        when (alg) {
            LineAlgorithm.EQUATION -> drawLineEquation(g)
            LineAlgorithm.DDA -> drawLineDDA(g)
            LineAlgorithm.MIDPOINT -> drawLineMidPoint(g)
            else -> drawLineGraphicsContext(g)
        }
    }

    override fun draw(gc: GraphicsContext) {
        drawLine(gc)
    }

    // region DRAW LINE ALGORITHMS

    private fun drawLineEquation(g: GraphicsContext) {
        // TODO
        val b = calculateYInterception()
        val m = calculateSlope()

        val deltaX = abs(p2.x - p1.x)
        val deltaY = abs(p2.y - p1.y)

//        x1 = x2 // deltaX = 0
//        y1 = y2 // deltaY = 0
        if (deltaX == 0.0 && deltaY == 0.0) {
            GraphicPoint(p1).drawPoint(g)
        }
//        deltaX > deltaY
        else if (deltaX > deltaY) {
            val minX = min(p1.x, p2.x).toInt()
            val maxX = max(p1.x, p2.x).toInt()

            for (x in minX..maxX) {
                GraphicPoint(
                    x,
                    calculateY(x.toDouble(), b, m).toInt(),
                    color,
                    width
                ).drawPoint(g)
            }
        }
//        deltaX = deltaY
        else if (deltaX == 0.0) {
            val minY = min(p1.y, p2.y).toInt()
            val maxY = max(p1.y, p2.y).toInt()

            for (y in minY..maxY) {
                GraphicPoint(
                    p1.x.toInt(),
                    y,
                    color,
                    width
                ).drawPoint(g)
            }
        }
//        deltaX < deltaY
        else {
            val minY = min(p1.y, p2.y).toInt()
            val maxY = max(p1.y, p2.y).toInt()

            for (y in minY..maxY) {
                val x = Line.calculateX(y.toDouble(), b, m)
                GraphicPoint(
                    x.toInt(),
                    y,
                    color,
                    width
                ).drawPoint(g)
            }
        }

        g.fill = color
        g.strokeText(name, p2.x + width, p2.y)

    }

    private fun drawLineDDA(g: GraphicsContext) {
        // TODO
    }

    private fun drawLineMidPoint(g: GraphicsContext) {
        // TODO
    }

    private fun drawLineGraphicsContext(g: GraphicsContext) {
        // TODO
    }

    // endregion

    // region CLIP LINE

    fun clip(rectangle: GraphicRectangle): Boolean {
        val min = rectangle.getMinPoint()
        val max = rectangle.getMaxPoint()

        val codeP1 = Clipping.classify(p1, min.x, min.y, max.x, max.y)
        val codeP2 = Clipping.classify(p2, min.x, min.y, max.x, max.y)

        if (Clipping.compare(codeP1, codeP2)) {
            return false
        }
        if ((codeP1 == Clipping.CENTER && codeP2 == Clipping.CENTER)) {
            return true
        }

        p1 = clipPoint(p1, codeP1, min, max)
        p2 = clipPoint(p2, codeP2, min, max)

        return true
    }

    private fun clipPoint(p: Point, code: Int, min: Point, max: Point): Point {
        var x = p.x
        var y = p.y
        val m = calculateSlope()

        if (code == Clipping.CENTER) {
            return p
        }

        if (Clipping.compare(code, Clipping.LEFT)) {
            y = y + (min.x - x) * m
            x = min.x
        }
        if (Clipping.compare(code, Clipping.RIGHT)) {
            y = y + (max.x - x) * m
            x = max.x
        }
        if (Clipping.compare(code, Clipping.TOP) && y < min.y) {
            x = x + (min.y - y) / m
            y = min.y
        }
        if (Clipping.compare(code, Clipping.BOTTOM) && y > max.y) {
            x = x + (max.y - y) / m
            y = max.y
        }

        return Point(x, y)
    }

    // endregion
}