package model.graphic.form

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import model.graphic.Form
import model.graphic.GraphicLine
import model.math.Point

class LineForm : Form {
    val minX: Double
    val minY: Double
    val maxX: Double
    val maxY: Double
    var color: Color = Color.BLACK
    var width = 1

    val lines = mutableListOf<GraphicLine>()

    constructor(minX: Double, minY: Double, maxX: Double, maxY: Double, divisions: Int, width: Int? = null, color: Color? = null) {
        this.minX = minX
        this.minY = minY
        this.maxX = maxX
        this.maxY = maxY
        width?.also { this.width = it }
        color?.also { this.color = it }
        createForm(divisions)
    }

    constructor(x: Double, y: Double, divisions: Int, width: Int? = null, color: Color? = null)
            : this(0.0, 0.0, x, y, divisions, width, color)

    fun createForm(divisions: Int) {
        val spacingX = (maxX - minX) / divisions
        val spacingY = (maxY - minY) / divisions

        for (count in 1..divisions) {
            val x1 = ((count - 1) * spacingX) + minX
            val y1 = ((count - 1) * spacingY) + minY

            val x2 = (count * spacingX) + minX
            val y2 = (count * spacingY) + minY

            // para fazer em um loop só
            val diff = divisions - count
            val x3 = (diff * spacingX) + minX
            val y3 = ((diff + 1) * spacingY) + minY

            lines.add(GraphicLine(minX, y1, x2, maxY, width, color))
            lines.add(GraphicLine(x1, minY, maxX, y2, width, color))
            lines.add(GraphicLine(minX, y3, x2, minY, width, color))
            lines.add(GraphicLine(x3, maxY, maxX, y1, width, color))
        }
    }

    override fun draw(gc: GraphicsContext) {
        lines.forEach { line -> line.draw(gc) }
    }

    override fun normalize(min: Point, max: Point): LineForm {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun convertFromNormalized(min: Point, max: Point) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}