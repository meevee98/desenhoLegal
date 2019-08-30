package controller

import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.event.EventHandler
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ColorPicker
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.stage.Stage
import model.enums.BasicForm

class MainWindowController {
    val title = "Bora Desenhar"
    var actualForm = BasicForm.POINT
    var drawDiameter: Int = 4
    val panePadding = 10

    val defaultColor = Color.BLACK
    var primaryColor: Color = Color.BLACK
    var secondaryColor: Color = Color.WHITE

    var canvasWidth: DoubleProperty
    var canvasHeight: DoubleProperty
    var diffHeight: DoubleProperty

    constructor() {
        this.canvasWidth = SimpleDoubleProperty(0.0)
        this.canvasHeight = SimpleDoubleProperty(0.0)
        this.diffHeight = SimpleDoubleProperty(0.0)
    }

    // region DataBinding

    fun bindColors(primaryPicker: ColorPicker, secondaryPicker: ColorPicker) {
        primaryPicker.value = primaryColor
        primaryPicker.onAction = EventHandler{
            primaryColor = primaryPicker.value
        }

        secondaryPicker.value = secondaryColor
        secondaryPicker.onAction = EventHandler{
            secondaryColor = secondaryPicker.value
        }

    }

    // endregion

    // region UpdateMainWindow

    fun updateWindowTitleWithCoordinates(window: Stage, event: MouseEvent) {
        val point = "(${event.x}, ${event.y})"
        window.title = "$title $point"
    }

    fun updateWindowTitleWithoutCoordinates(window: Stage) {
        window.title = title
    }

    // endregion

    // region SelectForm
    fun selectPoint() {
        actualForm = BasicForm.POINT
    }

    fun selectLine() {
        actualForm = BasicForm.LINE
        resetDraw()
    }

    fun selectCircle() {
        actualForm = BasicForm.CIRCLE
        resetDraw()
    }

    fun selectLineForm(input: String, context: GraphicsContext) {
        val divisions = input.toIntOrNull()

        if (divisions != null && divisions > 0) {
            Draw.Handler.drawLineForm(context, canvasWidth.get(), canvasHeight.get(), divisions, 1, primaryColor)
        }
    }

    fun resetDraw() {
        Draw.Handler.reset()
    }

    // endregion

    // region Actions

    fun clearCanvas(context: GraphicsContext) {
        // TODO não está funcionando chamar esse método a partir da MainWindow
        context.clearRect(0.0, 0.0, canvasWidth.get(), canvasHeight.get())
        resetDraw()
    }

    fun drawForm(context: GraphicsContext, event: MouseEvent) {
        // só responde ao clique se foi clicado apenas uma vez
        if (event.clickCount == 1) {
            val color = when (event.button) {
                MouseButton.PRIMARY -> primaryColor
                MouseButton.SECONDARY -> secondaryColor
                else -> defaultColor
            }

            Draw.Handler.drawForm(context, actualForm, event.x, event.y, drawDiameter, color)
        }
    }

    fun drawLineForm(context: GraphicsContext, div: String) {
        // TODO não está funcionando chamar esse método a partir da MainWindow
        val divisions = div.toIntOrNull()

        if (divisions != null && divisions > 0) {
            Draw.Handler.drawLineForm(
                    context,
                    canvasWidth.get(),
                    canvasHeight.get(),
                    divisions,
                    1,
                    primaryColor
            )
        }
    }

    // endregion

    // region Validation

    fun isIntGreaterThanZero(integer: String): Boolean {
        return integer.toIntOrNull() ?: 0 > 0
    }

    // endregion

}