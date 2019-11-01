package controller

import javafx.beans.property.*
import javafx.collections.FXCollections
import java.lang.Exception
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
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage
import model.constants.Constants
import model.enums.BasicForm
import model.math.Point
import util.FileHelper
import kotlin.math.max
import kotlin.math.min

class MainWindowController {
    // region Attributes
    val title = Constants.WINDOW_TITLE
    var actualForm = Constants.DEFAULT_GRAPHIC_FORM
    var drawDiameter: Int = Constants.DEFAULT_DRAW_DIAMETER
    val panePadding: Double = Constants.DEFAULT_PANE_PADDING

    val fileChooser = FileChooser().apply {
        for (extension in FileHelper.extensions) {
            extensionFilters.add(FileChooser.ExtensionFilter("${extension.key} (*.${extension.value})", "*.${extension.value}"))
        }
    }

    var clipping = SimpleBooleanProperty(false)
    var clippingActive = SimpleBooleanProperty(false)
    var clippingMin = SimpleObjectProperty<Point?>()
    var clippingMax = SimpleObjectProperty<Point?>()

    var primaryColor = SimpleObjectProperty(Constants.DEFAULT_PRIMARY_COLOR)
    var secondaryColor = SimpleObjectProperty(Constants.DEFAULT_SECONDARY_COLOR)

    val availableForms = SimpleListProperty<BasicForm>()
    val selectedForm = SimpleObjectProperty<BasicForm>()

    var canvasWidth: DoubleProperty = SimpleDoubleProperty(0.0)
    var canvasHeight: DoubleProperty = SimpleDoubleProperty(0.0)
    var diffWidth: DoubleProperty = SimpleDoubleProperty(0.0)
    var diffHeight: DoubleProperty = SimpleDoubleProperty(0.0)
    var borderWidth: DoubleProperty = SimpleDoubleProperty(0.0)
    var borderHeight: DoubleProperty = SimpleDoubleProperty(0.0)

    var mainCanvas = Canvas()
    var supportCanvas = Canvas()
    val canvasSnapshot = SimpleObjectProperty<Image>()

    init {
        canvasWidth.addListener { _ -> updateSnapshot() }
        canvasHeight.addListener { _ -> updateSnapshot() }
        clipping.addListener { obj -> clippingActive.set((obj as SimpleBooleanProperty).get())}

        availableForms.set(FXCollections.observableArrayList(BasicForm.values().asList()))
        selectedForm.set(availableForms.firstOrNull())
    }

    // endregion

    // region DataBinding

    fun bindColors(primaryPicker: ObjectProperty<Color>, secondaryPicker: ObjectProperty<Color> ) {
        primaryPicker.bindBidirectional(primaryColor)
        secondaryPicker.bindBidirectional(secondaryColor)
    }

    fun bindCanvasSize(scene: Scene) {
        // inicializa variaveis observables
        mainCanvas.boundsInParentProperty().addListener { observable, oldValue, newValue ->
            diffWidth.set(newValue.minX)
            diffHeight.set(newValue.minY)
        }

        mainCanvas.parent.boundsInParentProperty().addListener { observable, oldValue, newValue ->
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
        val point = "(${event.x.toInt()}, ${event.y.toInt()})"
        window.title = "$title $point"
    }

    fun updateWindowTitleWithoutCoordinates(window: Stage) {
        window.title = title
    }

    // endregion

    // region FileManager

    fun openFigureFromFile(stage: Stage) {
        fileChooser.showOpenDialog(stage)?.let {
            try {
                fileChooser.initialDirectory = it.parentFile
                FileHelper().readFile(
                        it,
                        Point(diffWidth.get(), diffHeight.get()),
                        Point(canvasWidth.get(), canvasHeight.get())
                )
                clearCanvas(mainCanvas.graphicsContext2D)
            }
            catch (e: Exception) {
                popupDialog(Constants.OPEN_FILE_ERROR, "Não foi possível abrir o arquivo")
            }
        }
    }

    fun saveFigureOnFile(stage: Stage) {
        fileChooser.showSaveDialog(stage)?.let {
            try {
                fileChooser.initialDirectory = it.parentFile
                FileHelper().writeFile(
                        it,
                        Point(diffWidth.get(), diffHeight.get()),
                        Point(canvasWidth.get(), canvasHeight.get())
                )
            }
            catch (e: Exception) {
                popupDialog(Constants.SAVE_FILE_ERROR, "Não foi possível salvar o arquivo")
            }
        }
    }

    // endregion

    // region SelectForm

    fun selectForm(form: BasicForm) {
        actualForm = form
        selectedForm.set(form)

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

    fun updateAvailableForms(forms: List<BasicForm>) {
        availableForms.clear()
        availableForms.addAll(forms)

        if (!forms.contains(actualForm)) {
            selectedForm.set(BasicForm.LINE)
        }
        else {
            selectedForm.set(actualForm)
        }
    }

    // endregion

    // region DrawOnCanvas

    private fun resetDraw() {
        val context = mainCanvas.graphicsContext2D
        DrawHandler.reset(context)
        FormStorage.redraw(context)
        updateSnapshot()
    }

    private fun cleanResetDraw() {
        clearCanvas(mainCanvas.graphicsContext2D)
        resetDraw()
    }

    fun clearCanvas(context: GraphicsContext) {
        context.clearRect(0.0, 0.0, Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT)
        resetDraw()
    }

    fun clear(context: GraphicsContext) {
        FormStorage.clear()
        clearCanvas(context)
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

    fun clip() {
        if (clipping.get()) {
            clippingMin.set(null)
            clippingMax.set(null)
            cleanResetDraw()
            clearCanvas(supportCanvas.graphicsContext2D)


            updateAvailableForms(BasicForm.values().asList())
        }
        else {
            val forms = listOf(
                    BasicForm.POINT,
                    BasicForm.LINE,
                    BasicForm.CIRCLE
            )
            updateAvailableForms(forms)
        }
    }

    fun selectClipArea(event: MouseEvent) {
        val context = supportCanvas.graphicsContext2D
        val point = DrawHandler.drawClipArea(context, event.x, event.y)

        if (clippingMin.get() == null) {
            clippingMin.set(point)
        }
        else if (clippingMax.get() == null) {
            val p2 = clippingMin.get() ?: return

            clippingMax.set(Point(
                    max(point.x, p2.x),
                    max(point.y, p2.y)
            ))
            clippingMin.set(Point(
                    min(point.x, p2.x),
                    min(point.y, p2.y)
            ))

            clippingActive.set(false)
            updateSnapshot()
        }
    }

    fun drawForm(context: GraphicsContext, event: MouseEvent) {
        if (clippingActive.get()) {
            selectClipArea(event)
        }
        else {
            val color = when (event.button) {
                MouseButton.PRIMARY -> primaryColor.get()
                MouseButton.SECONDARY -> secondaryColor.get()
                else -> Constants.DEFAULT_PRIMARY_COLOR
            }
            val area = clippingMin.get()?.let { min ->
                clippingMax.get()?.let { max ->
                    Rectangle2D(min.x, min.y, max.x - min.x, max.y - min.y)
                }
            }

            DrawHandler.drawForm(event.clickCount, context, area, actualForm, event.x, event.y, drawDiameter, color)
            updateSnapshot()
        }
    }

    private fun drawLineForm(context: GraphicsContext, input: String) {
        val divisions = input.toIntOrNull()
        val minX = clippingMin.get()?.x ?: 0.0
        val minY = clippingMin.get()?.y ?: 0.0
        val maxX = clippingMax.get()?.x ?: canvasWidth.get()
        val maxY = clippingMax.get()?.y ?: canvasHeight.get()
        val clipping = clipping.get()

        if (divisions != null && divisions > 0) {
            DrawHandler.drawLineForm(
                    context,
                    minX, minY,
                    maxX, maxY,
                    divisions,
                    Constants.HAIRLINE,
                    primaryColor.get(),
                    clipping
            )
            if (clipping) {
                cleanResetDraw()
            }
            else {
                clearCanvas(context)
            }
        }
        updateSnapshot()
    }

    fun updateSnapshot() {
        try {
            var minX = diffWidth.get()
            var minY = diffHeight.get()
            var maxX = canvasWidth.get()
            var maxY = canvasHeight.get()

            if (clippingMax.get() != null) {
                val clipMinX = min(maxX, clippingMin.get()?.x ?: 0.0)
                val clipMinY = min(maxY, clippingMin.get()?.y ?: 0.0)
                val clipMaxX = clippingMax.get()?.x ?: canvasWidth.get()
                val clipMaxY = clippingMax.get()?.y ?: canvasHeight.get()

                val width = min(maxX, clipMaxX) - clipMinX
                val height = min(maxY, clipMaxY) - clipMinY

                minX = min(maxX, clipMinX)
                minY = min(maxY, clipMinY)
                maxX = min(maxX, width)
                maxY = min(maxY, height)
            }

            val snapshot = if (maxX == 0.0 || maxY == 0.0) {
                null // só acontece quando a área do clip não está visivel, nesse caso a viewport fica vazia
            }
            else {
                val params = SnapshotParameters().apply {
                    viewport = Rectangle2D(minX, minY, maxX, maxY)
                }
                mainCanvas.snapshot(params, null)
            }
            canvasSnapshot.set(snapshot)
        }
        catch (e: Exception) {
            // essa exceção só acontece na inicialização, antes de disparar a scene
        }

    }

    // endregion

    // region Validation

    private fun isDivisionsAmountValid(integer: String): Boolean {
        // a regra atual é ser > 0 e <= 500
        // esse método toIntOrNull retorna null caso a String não seja um formato de número válido
        return (integer.toIntOrNull() ?: 0) in 1..500
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