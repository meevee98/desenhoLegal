package model.graphic

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class GraphicRectangle: Form {
    var color: Color = Color.BLACK
    var name = ""
    var width = 1
    var lines: List<GraphicLine> = listOf()

    constructor(p1: GraphicPoint, p2: GraphicPoint, width: Int? = null, color: Color? = null, name: String? = null) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
        calculateLines(p1.x.toInt(), p1.y.toInt(), p2.x.toInt(), p2.y.toInt())
    }


    constructor(x1: Int, y1: Int, x2: Int, y2: Int, width: Int? = null, color: Color? = null, name: String? = null) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
        calculateLines(x1, y1, x2, y2)
    }

    private fun calculateLines(x1: Int, y1: Int, x2: Int, y2: Int) {
        lines = listOf(
                GraphicLine(x1, y1, x1, y2, width, color, name),
                GraphicLine(x2, y2, x1, y2, width, color, name),
                GraphicLine(x2, y2, x2, y1, width, color, name),
                GraphicLine(x1, y1, x2, y1, width, color, name)
        )
    }


    override fun draw(gc: GraphicsContext) {
        lines.forEach { line ->
            line.draw(gc)
        }
    }
}
