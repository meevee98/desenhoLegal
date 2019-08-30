package controller

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import model.enums.BasicForm
import model.graphic.GraphicCircle
import model.graphic.GraphicLine
import model.graphic.GraphicPoint
import model.graphic.form.LineForm

class Draw {

    companion object Handler {
        private var pointIndex = 0
        private var lineIndex = 0
        private var circleIndex = 0

        private var firstPoint : GraphicPoint? = null
        private var firstColor : Color = Color.WHITE

        fun drawForm(g: GraphicsContext, form: BasicForm, x: Double, y: Double, diameter: Int, color: Color?, name: String?) {
            when (form) {
                BasicForm.POINT -> {
                    GraphicPoint(x, y, color, diameter, formatName(form, name)).drawPoint(g)
                }
                BasicForm.LINE -> {
                    val newPoint = GraphicPoint(x, y, color, diameter)
                    val p1 = firstPoint ?: newPoint

                    if (firstPoint == null) {
                        // se firstPoint == null, define o novo ponto como o ponto inicial da reta
                        firstPoint = newPoint
                        firstColor = newPoint.pointColor
                        newPoint.drawPoint(g)
                    }
                    else {
                        // se o firstPoint não for null, traça uma reta entre o firstPoint e o novo ponto
                        GraphicLine.draw(g, p1.x, p1.y, x, y, diameter, firstColor, formatName(form, name))

                        // retorna o firstPoint para null para que não seja traçada um reta entre este e o próximo ponto
                        firstPoint = null
                    }
                }
                BasicForm.CIRCLE -> {
                    val newPoint = GraphicPoint(x, y)
                    val c = firstPoint ?: newPoint

                    if (firstPoint == null) {
                        // se firstPoint == null, define o novo ponto como o centro do circulo
                        firstPoint = newPoint
                    }
                    else {
                        // se o firstPoint não for null, traça um circulo com centro no firstPoint
                        // e raio = distancia entre o firstPoint e o novo ponto
                        val radius = c.calculateDistance(newPoint)
                        GraphicCircle.draw(g, c.x, c.y, radius, diameter, color, formatName(form, name))

                        // retorna o firstPoint para null para que não seja traçado um circulo com centro no novo ponto
                        firstPoint = null
                    }
                }
                else -> return
            }
        }

        fun drawLineForm(g: GraphicsContext, x: Double, y: Double, divisions: Int, diameter: Int, color: Color?) {
            LineForm(x.toInt(), y.toInt(), diameter, color)
                    .drawForm(g, divisions)
        }

        fun reset() {
            firstPoint = null
            firstColor = Color.WHITE
        }

        private fun drawPoint(g: GraphicsContext, x: Int, y: Int, diameter: Int, name: String?, color: Color?) {
            // Cria um ponto
            val point = GraphicPoint(x.toDouble(), y.toDouble(), color, diameter, name)

            // Desenha o ponto
            point.drawPoint(g)
        }

        // TODO : Avaliar o nome desse método
        private fun formatName(form: BasicForm, name: String?): String {
            return name ?: ""
        }

    }
}