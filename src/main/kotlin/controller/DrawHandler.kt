package controller

import javafx.geometry.Rectangle2D
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import model.constants.Constants
import model.enums.BasicForm
import model.graphic.*
import model.graphic.form.LineForm
import kotlin.math.abs
import kotlin.math.min

object DrawHandler {
    private var firstPoint: GraphicPoint? = null
    private var firstColor: Color = Color.WHITE
    private var polygon: GraphicPolygon? = null

    fun drawForm(clickCount: Int, g: GraphicsContext, clippingArea: Rectangle2D? = null, form: BasicForm, x: Double, y: Double, diameter: Int, color: Color? = null, name: String? = null) {
        when (form) {
            BasicForm.POINT -> drawPoint(g, x, y, diameter, formatName(form, name), color, clippingArea)
            BasicForm.LINE -> drawLine(clickCount, g, x, y, diameter, formatName(form, name), color, clippingArea)
            BasicForm.CIRCLE -> drawCircle(clickCount, g, x, y, diameter, formatName(form, name), color, clippingArea)
            BasicForm.RECTANGLE -> drawRectangle(clickCount, g, x, y, diameter, formatName(form, name), color, clippingArea)
            BasicForm.POLYGON -> drawPolygon(clickCount, g, x, y, diameter, formatName(form, name), color, clippingArea)
            else -> return
        }
    }

    fun drawLineForm(g: GraphicsContext, minX: Double, minY: Double, maxX: Double, maxY: Double, divisions: Int, diameter: Int, color: Color?, clipping: Boolean) {
        val lineForm = LineForm(minX, minY, maxX, maxY, divisions, diameter, color)

        if (!clipping) {
            FormStorage.drawClear(lineForm, g)
        }
        else {
            FormStorage.draw(lineForm, g)
        }
    }

    fun drawClipArea(g: GraphicsContext, x: Double, y: Double): GraphicPoint {
        val newPoint = GraphicPoint(x, y)
        val p1 = firstPoint ?: newPoint

        if (firstPoint == null) {
            // se firstPoint == null, define o novo ponto como o ponto inicial da reta
            firstPoint = newPoint
        }
        else {
            shapeClipRect(g, p1, x, y)
            firstPoint = null
        }

        return newPoint
    }

    private fun shapeClipRect(context: GraphicsContext, p: GraphicPoint, x: Double, y: Double) {
        val hTotal = context.canvas.height
        val wTotal = context.canvas.width

        val wClipped = abs(p.x - x)
        val hClipped = abs(p.y - y)

        val minX = min(p.x, x)
        val minY = min(p.y, y)

        context.fill = Constants.DEFAULT_SELECTION_COLOR
                .deriveColor(1.0, 1.0, 1.0, 0.8) // opacidade em 80%
                // as alterações são em porcentagem // 1.0 -> não alterar
        context.fillRect(0.0, 0.0, wTotal, hTotal)
        context.clearRect(minX, minY, wClipped, hClipped)
    }

    fun reset(g: GraphicsContext) {
        firstPoint = null
        firstColor = Color.WHITE
        polygon = polygon?.let {
            FormStorage.draw(it, g)
            null
        }
    }

    // TODO : Avaliar o nome desse método
    private fun formatName(form: BasicForm, name: String?): String {
        return name ?: ""
    }

    // region Point

    private fun drawPoint(g: GraphicsContext, x: Double, y: Double, diameter: Int, name: String?, color: Color? = null, clip: Rectangle2D?) {
        val point = GraphicPoint(x, y, color, diameter, name)

        if (clip != null && !isInsideClipping(point, clip)) {
            return
        }

        FormStorage.draw(point, g)
    }

    private fun isInsideClipping(p: GraphicPoint, area: Rectangle2D?): Boolean {
        area?.run {
            if (p.x < minX || p.x > maxX) {
                return false
            }
            if (p.y < minY || p.y > maxY) {
                return false
            }
        }
        return true
    }

    // endregion

    // region Line

    private fun drawLine(clickCount: Int, g: GraphicsContext, x: Double, y: Double, diameter: Int, name: String?, color: Color? = null, clip: Rectangle2D?) {
        if (clickCount == 1) {
            val newPoint = GraphicPoint(x, y, color, diameter)
            val p1 = firstPoint ?: newPoint

            if (firstPoint == null) {
                // se firstPoint == null, define o novo ponto como o ponto inicial da reta
                firstPoint = newPoint
                firstColor = newPoint.pointColor
                if (clip != null && !isInsideClipping(newPoint, clip)) {
                    return
                }
                newPoint.drawPoint(g)
            } else {
                // se o firstPoint não for null, traça uma reta entre o firstPoint e o novo ponto
                val p2 = GraphicPoint(x, y)
                val line = clip?.let { area ->
                    clipLine(p1, p2, diameter, name, firstColor, area)
                } ?: GraphicLine(p1, p2, diameter, firstColor, name)
                FormStorage.draw(line, g)

                // retorna o firstPoint para null para que não seja traçada um reta entre este e o próximo ponto
                firstPoint = null
            }
        }
    }

    private fun clipLine(p1: GraphicPoint, p2: GraphicPoint, diameter: Int, name: String?, color: Color? = null, clipArea: Rectangle2D): GraphicLine {
        val line = GraphicLine(p1, p2, diameter, firstColor, name)

        val min = GraphicPoint(clipArea.minX, clipArea.minY)
        val max = GraphicPoint(clipArea.maxX, clipArea.maxY)

        line.clip(GraphicRectangle(min, max))

        return line
    }

    // endregion

    // region Circle

    private fun drawCircle(clickCount: Int, g: GraphicsContext, x: Double, y: Double, diameter: Int, name: String?, color: Color? = null, clip: Rectangle2D?) {
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
                val circle: Form = clip?.let { area ->
                    clipCircle(c, radius, diameter, name, color, area)
                } ?: GraphicCircle(c, radius, diameter, color, name)
                FormStorage.draw(circle, g)

                // retorna o firstPoint para null para que não seja traçado um circulo com centro no novo ponto
                firstPoint = null
            }
        }
    }

    private fun clipCircle(center: GraphicPoint, radius: Double, diameter: Int, name: String?, color: Color? = null, clip: Rectangle2D): GraphicArc {
        val min = GraphicPoint(clip.minX, clip.minY)
        val max = GraphicPoint(clip.maxX, clip.maxY)

        return GraphicArc(center, radius, min, max, diameter, color, name)
    }

    // endregion

    // region Rectangle

    private fun drawRectangle(clickCount: Int, g: GraphicsContext, x: Double, y: Double, diameter: Int, name: String?, color: Color? = null, clip: Rectangle2D?) {
        // não desenhar retangulo quando clipping está ligado
        if (clip == null && clickCount == 1) {
            val newPoint = GraphicPoint(x, y, color, diameter)
            val p1 = firstPoint ?: newPoint

            if (firstPoint == null) {
                // se firstPoint == null, define o novo ponto como o ponto inicial da reta
                firstPoint = newPoint
                firstColor = newPoint.pointColor
                newPoint.drawPoint(g)
            }
            else {
                val rectangle = GraphicRectangle(p1, GraphicPoint(x, y), diameter, firstColor, name)
                FormStorage.draw(rectangle, g)

                // retorna o firstPoint para null para que não seja traçada um retangulo entre este e o próximo ponto
                firstPoint = null
            }
        }
    }

    private fun drawPolygon(clickCount: Int, g: GraphicsContext, x: Double, y: Double, diameter: Int, name: String?, color: Color? = null, clip: Rectangle2D?) {
        // não desenhar poligono quando clipping está ligado
        if (clip == null) {
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
    }

    // endregion
}