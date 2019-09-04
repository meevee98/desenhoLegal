package controller

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ColorPicker
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage
import model.constants.Constants
import model.enums.BasicForm

class MainWindowController {
    val title = Constants.WINDOW_TITLE
    var actualForm = Constants.DEFAULT_GRAPHIC_FORM
    var drawDiameter: Int = Constants.DEFAULT_DRAW_DIAMETER
    val panePadding: Double = Constants.DEFAULT_PANE_PADDING

    var primaryColor = SimpleObjectProperty<Color>(Constants.DEFAULT_PRIMARY_COLOR)
    var secondaryColor = SimpleObjectProperty<Color>(Constants.DEFAULT_SECONDARY_COLOR)

    var canvasWidth: DoubleProperty
    var canvasHeight: DoubleProperty
    var diffHeight: DoubleProperty

    constructor() {
        this.canvasWidth = SimpleDoubleProperty(0.0)
        this.canvasHeight = SimpleDoubleProperty(0.0)
        this.diffHeight = SimpleDoubleProperty(0.0)
    }

    // region DataBinding

    fun bindColors(primaryPicker: ObjectProperty<Color>, secondaryPicker: ObjectProperty<Color> ) {
        primaryPicker.bindBidirectional(primaryColor)
        secondaryPicker.bindBidirectional(secondaryColor)
    }

    fun bindCanvasSize(pane: Pane, canvas: Canvas) {
        // inicializa variaveis observables
        canvas.widthProperty().bindBidirectional(canvasWidth)
        canvas.heightProperty().bindBidirectional(canvasHeight)

        // Conecta o tamanho do canvas ao tamanho do painel
        canvas.widthProperty().bind(
                pane.widthProperty())
        canvas.heightProperty().bind(
                pane.heightProperty().subtract(diffHeight))
    }

    fun bindHeight(pane: Pane) {
        diffHeight.bind(pane.heightProperty().add(panePadding))
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
            DrawHandler.drawLineForm(context, canvasWidth.get(), canvasHeight.get(), divisions, 1, primaryColor.get())
        }
    }

    fun resetDraw() {
        DrawHandler.reset()
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
                MouseButton.PRIMARY -> primaryColor.get()
                MouseButton.SECONDARY -> secondaryColor.get()
                else -> Constants.DEFAULT_PRIMARY_COLOR
            }

            DrawHandler.drawForm(context, actualForm, event.x, event.y, drawDiameter, color)
        }
    }

    fun drawLineForm(context: GraphicsContext, div: String) {
        // TODO não está funcionando chamar esse método a partir da MainWindow
        val divisions = div.toIntOrNull()

        if (divisions != null && divisions > 0) {
            DrawHandler.drawLineForm(
                    context,
                    canvasWidth.get(),
                    canvasHeight.get(),
                    divisions,
                    1,
                    primaryColor.get()
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