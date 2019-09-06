package controller

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.TextInputDialog
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage
import model.constants.Constants
import model.enums.BasicForm

class MainWindowController {
    // region Attributes

    val title = Constants.WINDOW_TITLE
    var actualForm = Constants.DEFAULT_GRAPHIC_FORM
    var drawDiameter: Int = Constants.DEFAULT_DRAW_DIAMETER
    val panePadding: Double = Constants.DEFAULT_PANE_PADDING

    var primaryColor = SimpleObjectProperty(Constants.DEFAULT_PRIMARY_COLOR)
    var secondaryColor = SimpleObjectProperty(Constants.DEFAULT_SECONDARY_COLOR)

    var canvasWidth: DoubleProperty = SimpleDoubleProperty(0.0)
    var canvasHeight: DoubleProperty = SimpleDoubleProperty(0.0)
    var diffWidth: DoubleProperty = SimpleDoubleProperty(0.0)
    var diffHeight: DoubleProperty = SimpleDoubleProperty(0.0)

    // endregion

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
                pane.widthProperty().subtract(diffWidth))
        canvas.heightProperty().bind(
                pane.heightProperty().subtract(diffHeight))
    }

    fun bindHeight(pane: Pane) {
        diffHeight.bind(pane.heightProperty().add(panePadding).add(diffHeight.value))
    }

    fun bindWidth(pane: Pane) {
        diffWidth.bind(pane.widthProperty().add(panePadding).add(diffWidth.value))
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

    fun selectLineForm(context: GraphicsContext) {
        TextInputDialog().run {
            graphic = null // remove o icone - null não é o default
            title = "Desenhar Forma"
            headerText = "Insira o número de divisões"
            contentText = "Divisões:"

            showAndWait()
        }.ifPresent { input ->
            if (isDivisionsAmountValid(input)) {
                drawLineForm(context, input)
            }
            else {
                popupDialog(Constants.INVALID_INPUT, "Valor inserido \"$input\" é inválido para quantidade de divisões")
            }
        }
    }

    // endregion

    // region DrawOnCanvas

    private fun resetDraw() {
        DrawHandler.reset()
    }

    fun clearCanvas(context: GraphicsContext) {
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

    private fun drawLineForm(context: GraphicsContext, input: String) {
        val divisions = input.toIntOrNull()

        if (divisions != null && divisions > 0) {
            clearCanvas(context)
            DrawHandler.drawLineForm(
                    context,
                    canvasWidth.get(),
                    canvasHeight.get(),
                    divisions,
                    Constants.HAIRLINE,
                    primaryColor.get()
            )
        }
    }

    // endregion

    // region Validation

    private fun isDivisionsAmountValid(integer: String): Boolean {
        // a regra atual é ser > 0
        // esse método toIntOrNull retorna null caso a String não seja um formato de número válido
        return integer.toIntOrNull() ?: 0 > 0
    }

    fun popupDialog(dialogTitle: String, dialogContent: String, dialogType: AlertType = AlertType.CONFIRMATION) {
        Alert(dialogType).run {
            graphic = null // remove o icone - null não é o default
            title = dialogTitle
            contentText = dialogContent
            showAndWait()
        }
    }

    // endregion

}