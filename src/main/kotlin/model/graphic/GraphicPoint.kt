package model.graphic

import model.math.Point
import java.awt.Color
import java.awt.Graphics

class GraphicPoint : Point {
    //TODO: change java.awt to TornadoFX
    var pointColor: Color = Color.BLACK
    var nameColor: Color = Color.BLACK
    var name = ""
    var diameter = 1

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
        diameter?.also { this.diameter = it }
    }

    // endregion

    // region DRAW

    /**
     * desenha um ponto utilizando o oval
     *
     * @param g contexto grafico
     */
    fun drawPoint(g: Graphics) {
        g.color = pointColor
        g.fillOval(x.toInt() - diameter/2, y.toInt() - diameter/2, diameter, diameter)

        g.color = nameColor
        g.drawString(name, x.toInt() + diameter, y.toInt())
    }

    // endregion
}