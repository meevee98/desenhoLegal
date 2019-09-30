package controller

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import model.enums.BasicForm
import model.graphic.*
import model.graphic.form.LineForm

class DrawHandler {

    companion object {
        private var firstPoint: GraphicPoint? = null
        private var firstColor: Color = Color.WHITE
        private var polygon: GraphicPolygon? = null

        fun drawForm(clickCount: Int, g: GraphicsContext, form: BasicForm, x: Double, y: Double, diameter: Int, color: Color? = null, name: String? = null) {
            when (form) {
                BasicForm.POINT -> {
                    // só responde ao clique se foi clicado apenas uma vez
                    if (clickCount == 1) {
                        val point = GraphicPoint(x, y, color, diameter, formatName(form, name))
                        FormStorage.draw(point, g)
                    }
                }
                BasicForm.LINE -> {
                    // só responde ao clique se foi clicado apenas uma vez
                    if (clickCount == 1) {
                        val newPoint = GraphicPoint(x, y, color, diameter)
                        val p1 = firstPoint ?: newPoint

                        if (firstPoint == null) {
                            // se firstPoint == null, define o novo ponto como o ponto inicial da reta
                            firstPoint = newPoint
                            firstColor = newPoint.pointColor
                            newPoint.drawPoint(g)
                        } else {
                            // se o firstPoint não for null, traça uma reta entre o firstPoint e o novo ponto
                            val line = GraphicLine(p1, GraphicPoint(x, y), diameter, firstColor, formatName(form, name))
                            FormStorage.draw(line, g)

                            // retorna o firstPoint para null para que não seja traçada um reta entre este e o próximo ponto
                            firstPoint = null
                        }
                    }
                }
                BasicForm.CIRCLE -> {
                    // só responde ao clique se foi clicado apenas uma vez
                    if (clickCount == 1) {
                        val newPoint = GraphicPoint(x, y)
                        val c = firstPoint ?: newPoint

                        if (firstPoint == null) {
                            // se firstPoint == null, define o novo ponto como o centro do circulo
                            firstPoint = newPoint
                        } else {
                            // se o firstPoint não for null, traça um circulo com centro no firstPoint
                            // e raio = distancia entre o firstPoint e o novo ponto
                            val radius = c.calculateDistance(newPoint)
                            val circle = GraphicCircle(c, radius, diameter, color, formatName(form, name))
                            FormStorage.draw(circle, g)

                            // retorna o firstPoint para null para que não seja traçado um circulo com centro no novo ponto
                            firstPoint = null
                        }
                    }
                }
                BasicForm.RECTANGLE -> {
                    // só responde ao clique se foi clicado apenas uma vez
                    if (clickCount == 1) {
                        val newPoint = GraphicPoint(x, y, color, diameter)
                        val p1 = firstPoint ?: newPoint

                        if (firstPoint == null) {
                            // se firstPoint == null, define o novo ponto como o ponto inicial da reta
                            firstPoint = newPoint
                            firstColor = newPoint.pointColor
                            newPoint.drawPoint(g)
                        } else {
                            // se o firstPoint não for null, traça uma reta entre o firstPoint e o novo ponto
                            val line = GraphicRectangle(p1, GraphicPoint(x, y), diameter, firstColor, formatName(form, name))
                            FormStorage.draw(line, g)

                            // retorna o firstPoint para null para que não seja traçada um reta entre este e o próximo ponto
                            firstPoint = null
                        }
                    }
                }
                BasicForm.POLYGON -> {
                    // só responde ao clique se foi clicado apenas uma vez
                    if (clickCount == 1) {
                        if (polygon == null) {
                            polygon = GraphicPolygon(diameter, color, name)
                        }
                        polygon?.addPoint(x, y, g)
                    }
                    // duplo clique fecha o polígono
                    else if (clickCount == 2 && polygon != null) {
                        polygon = polygon?.let {
                            FormStorage.draw(it, g)
                            null
                        }
                    }
                }
                else -> return
            }
        }

        fun drawLineForm(g: GraphicsContext, x: Double, y: Double, divisions: Int, diameter: Int, color: Color?) {
            val lineForm = LineForm(x, y, divisions, diameter, color)
            FormStorage.drawClear(lineForm, g)
        }

        fun reset(g: GraphicsContext) {
            firstPoint = null
            firstColor = Color.WHITE
            polygon = polygon?.let {
                FormStorage.draw(it, g)
                null
            }
        }

        private fun drawPoint(g: GraphicsContext, x: Int, y: Int, diameter: Int, name: String?, color: Color?) {
            // Cria um ponto
            val point = GraphicPoint(x, y, color, diameter, name)

            // Desenha o ponto
            point.drawPoint(g)
        }

        // TODO : Avaliar o nome desse método
        private fun formatName(form: BasicForm, name: String?): String {
            return name ?: ""
        }

    }
}