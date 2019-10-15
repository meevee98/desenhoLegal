package model.graphic

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class GraphicRectangle: Form {
    var color: Color = Color.BLACK
    var name = ""
    var width = 1
    val p1: GraphicPoint
    val p2: GraphicPoint

    constructor(p1: GraphicPoint, p2: GraphicPoint, width: Int? = null, color: Color? = null, name: String? = null) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
        this.p1 = p1
        this.p2 = p2
    }


    constructor(x1: Int, y1: Int, x2: Int, y2: Int, width: Int? = null, color: Color? = null, name: String? = null) {
        width?.also { this.width = it }
        color?.also { this.color = it }
        name?.also { this.name = it }
        this.p1 = GraphicPoint(x1.toDouble(), y1.toDouble())
        this.p2 = GraphicPoint(x2.toDouble(), y2.toDouble())
    }

    private fun calculateLines(): List<GraphicLine> {
        return listOf(
                GraphicLine(p1.x, p1.y, p1.x, p2.y, width, color, name),
                GraphicLine(p2.x, p2.y, p1.x, p2.y, width, color, name),
                GraphicLine(p2.x, p2.y, p2.x, p1.y, width, color, name),
                GraphicLine(p1.x, p1.y, p2.x, p1.y, width, color, name)
        )
    }


    override fun draw(gc: GraphicsContext) {
        calculateLines().forEach { line ->
            line.draw(gc)
        }
    }
}
