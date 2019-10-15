package controller

import java.lang.Exception
import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Rectangle2D
import javafx.scene.Scene
import javafx.scene.SnapshotParameters
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.TextInputDialog
import javafx.scene.image.Image
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
    var borderWidth: DoubleProperty = SimpleDoubleProperty(0.0)
    var borderHeight: DoubleProperty = SimpleDoubleProperty(0.0)

    var canvas = Canvas()
    val canvasSnapshot = SimpleObjectProperty<Image>()

    init {
        canvasWidth.addListener { _ -> updateSnapshot() }
        canvasHeight.addListener { _ -> updateSnapshot() }
    }

    // endregion

    // region DataBinding

    fun bindColors(primaryPicker: ObjectProperty<Color>, secondaryPicker: ObjectProperty<Color> ) {
        primaryPicker.bindBidirectional(primaryColor)
        secondaryPicker.bindBidirectional(secondaryColor)
    }

    fun bindCanvasSize(scene: Scene) {
        // inicializa variaveis observables
        canvas.boundsInParentProperty().addListener { observable, oldValue, newValue ->
            diffWidth.set(newValue.minX)
            diffHeight.set(newValue.minY)
        }

        canvas.parent.boundsInParentProperty().addListener { observable, oldValue, newValue ->
            val diffX = newValue.minX - oldValue.minX
            val diffY = newValue.minY - oldValue.minY
            borderWidth.set(borderWidth.doubleValue() + diffX)
            borderHeight.set(borderHeight.doubleValue() + diffY)
        }

        canvasWidth.bind(scene.widthProperty().subtract(diffWidth).subtract(borderWidth))
        canvasHeight.bind(scene.heightProperty().subtract(diffHeight).subtract(borderHeight))

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
        cleanResetDraw()
    }

    fun selectLine() {
        actualForm = BasicForm.LINE
        cleanResetDraw()
    }

    fun selectCircle() {
        actualForm = BasicForm.CIRCLE
        cleanResetDraw()
    }

    fun selectRectangle() {
        actualForm = BasicForm.RECTANGLE
        cleanResetDraw()
    }

    fun selectPolygon() {
        actualForm = BasicForm.POLYGON
        cleanResetDraw()
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
        val context = canvas.graphicsContext2D
        DrawHandler.reset(context)
        FormStorage.redraw(context)
        updateSnapshot()
    }

    private fun cleanResetDraw() {
        clearCanvas(canvas.graphicsContext2D)
        resetDraw()
    }

    fun clearCanvas(context: GraphicsContext) {
        context.clearRect(0.0, 0.0, Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT)
        resetDraw()
    }

    fun clear(context: GraphicsContext) {
        clearCanvas(context)
        FormStorage.clear()
    }

    fun undo(context: GraphicsContext) {
        if( FormStorage.undo() ) {
            clearCanvas(context)
            FormStorage.redraw(context)
        }
        resetDraw()
    }

    fun redo(context: GraphicsContext) {
        if( FormStorage.redo() ) {
            clearCanvas(context)
            FormStorage.redraw(context)
        }
        resetDraw()
    }

    fun clip(context: GraphicsContext) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun drawForm(context: GraphicsContext, event: MouseEvent) {
        val color = when (event.button) {
            MouseButton.PRIMARY -> primaryColor.get()
            MouseButton.SECONDARY -> secondaryColor.get()
            else -> Constants.DEFAULT_PRIMARY_COLOR
        }

        DrawHandler.drawForm(event.clickCount, context, actualForm, event.x, event.y, drawDiameter, color)
        updateSnapshot()
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
        updateSnapshot()
    }

    fun updateSnapshot() {
        try {
            val params = SnapshotParameters().apply {
                viewport = Rectangle2D(diffWidth.value, diffHeight.value, canvasWidth.value, canvasHeight.value)
            }
            canvasSnapshot.value = canvas.snapshot(params, null)
        }
        catch (e: Exception) {
            // essa exceção só acontece na inicialização, antes de disparar a scene
            // ignorar por enquanto
        }

    }

    // endregion

    // region Validation

    private fun isDivisionsAmountValid(integer: String): Boolean {
        // a regra atual é ser > 0 e <= 500
        // esse método toIntOrNull retorna null caso a String não seja um formato de número válido
        return integer.toIntOrNull() ?: 0 in 1..500
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