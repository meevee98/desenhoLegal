package model.graphic

import javafx.scene.paint.Color
import javafx.scene.canvas.GraphicsContext
import model.math.Point

class GraphicPoint : Point, Form {
    var pointColor: Color = Color.BLACK
    var nameColor: Color = Color.BLACK
    var name = ""
    var diameter = 1.0

    // region CONSTRUCTORS

    constructor(x: Double,
                y: Double,
                color: Color? = null,
                diameter: Int? = null,
                name: String? = null)
            : super(x, y) {
        color?.also {
            this.pointColor = it
            this.nameColor = it
        }
        name?.also { this.name = it }
        diameter?.also { this.diameter = it.toDouble() }
    }

    constructor(x: Int,
                y: Int,
                color: Color? = null,
                diameter: Int? = null,
                name: String? = null)
            : this(x.toDouble(), y.toDouble(), color, diameter, name)

    constructor(point: Point) : super(point)

    // endregion

    // region DRAW

    /**
     * desenha um ponto utilizando o oval
     *
     * @param g contexto grafico
     */
    fun drawPoint(g: GraphicsContext) {
        g.fill = pointColor
        g.fillOval(x.toInt() - diameter / 2, y - diameter / 2, diameter, diameter)

        g.fill = nameColor
        g.strokeText(name, x.toInt() + diameter, y)
    }

    override fun draw(gc: GraphicsContext) {
        drawPoint(gc)
    }

    // endregion
}