package model.graphic.form

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import model.graphic.Form
import model.graphic.GraphicLine

class LineForm : Form {
    val x: Double
    val y: Double
    var color: Color = Color.BLACK
    var width = 1

    val lines = mutableListOf<GraphicLine>()

    constructor(x: Double, y: Double, divisions: Int, width: Int? = null, color: Color? = null){
        this.x = x
        this.y = y
        width?.also { this.width = it }
        color?.also { this.color = it }
        createForm(divisions)
    }

    fun createForm(divisions: Int) {
        val spacingX = x.toDouble() / divisions
        val spacingY = y.toDouble() / divisions

        for (count in 1..divisions) {
            val x1 = ((count - 1) * spacingX)
            val y1 = ((count - 1) * spacingY)

            val x2 = (count * spacingX)
            val y2 = (count * spacingY)

            // para fazer em um loop só
            val diff = divisions - count
            val x3 = (diff * spacingX)
            val y3 = ((diff + 1) * spacingY)

            lines.add(GraphicLine(0.0, y1, x2, y, width, color))
            lines.add(GraphicLine(x1, 0.0, x, y2, width, color))
            lines.add(GraphicLine(0.0, y3, x2, 0.0, width, color))
            lines.add(GraphicLine(x3, y, x, y1, width, color))
        }
    }

    override fun draw(gc: GraphicsContext) {
        lines.forEach { line -> line.draw(gc) }
    }
}