package model.graphic.form

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import model.graphic.GraphicLine

class LineForm {
    val x: Int
    val y: Int
    var color: Color = Color.BLACK
    var width = 1

    constructor(x: Int, y: Int, width: Int? = null, color: Color? = null){
        this.x = x
        this.y = y
        width?.also { this.width = it }
        color?.also { this.color = it }
    }

    fun drawForm(g: GraphicsContext, divisions: Int) {
        val spacingX = x.toDouble() / divisions
        val spacingY = y.toDouble() / divisions

        for (count in 1..divisions) {
            val x1 = ((count - 1) * spacingX).toInt()
            val y1 = ((count - 1) * spacingY).toInt()

            val x2 = (count * spacingX).toInt()
            val y2 = (count * spacingY).toInt()

            // para fazer em um loop só
            val diff = divisions - count
            val x3 = (diff * spacingX).toInt()
            val y3 = ((diff + 1) * spacingY).toInt()

            GraphicLine(0, y1, x2, y, width, color).drawLine(g)
            GraphicLine(x1, 0, x, y2, width, color).drawLine(g)
            GraphicLine(0, y3, x2, 0, width, color).drawLine(g)
            GraphicLine(x3, y, x, y1, width, color).drawLine(g)
        }
    }
}