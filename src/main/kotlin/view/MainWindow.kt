package view

import controller.MainWindowController
import controller.DrawHandler
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.ColorPicker
import javafx.scene.control.TextInputDialog
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import model.constants.Constants

class MainWindow(private val controller: MainWindowController, stage: Stage) {

    // TODO: Guardar as constantes em outro lugar
    private val panePadding = Constants.DEFAULT_PANE_PADDING

    private var canvasWidth: DoubleProperty = SimpleDoubleProperty(0.0)
    private var canvasHeight: DoubleProperty = SimpleDoubleProperty(0.0)
    private var diffHeight: DoubleProperty = SimpleDoubleProperty(0.0)

    init {
        iniciateWindow(stage)
    }

    fun iniciateWindow(stage: Stage) {
        stage.run {
            // define titulo da janela
            title = Constants.WINDOW_TITLE

            // define largura e altura da janela
            width = 500.0
            height = 500.0
        }

        // Painel para os componentes
        val pane = VBox(controller.panePadding)

        // componente para desenho
        val canvas = Canvas()

        // componente para desenhar graficos
        val gc = canvas.graphicsContext2D

        // componente para os botões
        val buttons = HBox(5.0)
        includeButtons(buttons, gc)
        controller.bindHeight(buttons)

        // Eventos de mouse
        canvas.setOnMouseMoved { event -> controller.updateWindowTitleWithCoordinates(stage, event) }
        canvas.setOnMouseExited { controller.updateWindowTitleWithoutCoordinates(stage) }
        canvas.setOnMousePressed { event -> controller.drawForm(gc, event) }

        // atributos do painel
        pane.background = Background(BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY))
        pane.children.addAll(buttons, canvas) // posiciona o componente de desenho
        controller.bindCanvasSize(pane, canvas)

        // cria e insere cena
        val scene = Scene(pane)
        stage.scene = scene
        stage.show()
    }

    private fun includeButtons(buttons: HBox, gc: GraphicsContext) {
        val point = Button().apply { // desenhar pontos
            text = "Ponto"
            setOnAction { controller.selectPoint() }
        }

        val line = Button().apply { // desenhar linhas
            text = "Reta"
            setOnAction { controller.selectLine() }
        }

        val circle = Button().apply { // desenhar circulos
            text = "Círculo"
            setOnAction { controller.selectCircle() }
        }

        val drawForm = Button().apply {
            text = "Outras Formas"
            setOnAction { click -> drawFormDialog(gc) } // TODO: passar OnActionListener para o controller
        }

        val clear = Button().apply { // limpar canvas
            text = "Limpar"
            setOnAction { clear(gc) } // TODO: passar OnActionListener para o controller
        }

        // color choices
        val primaryColorPicker = ColorPicker()
        val secondaryColorPicker = ColorPicker()
        controller.bindColors(
                primaryColorPicker.valueProperty(),
                secondaryColorPicker.valueProperty()
        )

        val colors = VBox(5.0)
        colors.children.addAll(primaryColorPicker, secondaryColorPicker)
        colors.minHeight = 60.0

        buttons.children.addAll(point, line, circle, drawForm, clear, colors)
    }

    private fun clear(gc: GraphicsContext) {
        gc.clearRect(0.0, 0.0, controller.canvasWidth.get(), controller.canvasHeight.get())
        controller.resetDraw()
    }

    private fun drawFormDialog(gc: GraphicsContext) {
        val drawFormDialog = TextInputDialog()

        drawFormDialog.title = "Desenhar Forma"
        drawFormDialog.headerText = "Insira o número de divisões"
        drawFormDialog.contentText = "Divisões:"

        drawFormDialog.showAndWait().ifPresent { div ->
            run {
                drawLineForm(gc, div)
            }
        }
    }

    fun drawLineForm(context: GraphicsContext, div: String) {
        val divisions = div.toIntOrNull()

        if (divisions != null && divisions > 0) {
            clear(context)
            DrawHandler.drawLineForm(
                    context,
                    controller.canvasWidth.value,
                    controller.canvasHeight.value,
                    divisions,
                    1,
                    controller.primaryColor.get()
            )
        }
    }

}
