package view

import controller.MainWindowController
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.ColorPicker
import javafx.scene.layout.*
import javafx.stage.Stage
import model.constants.Constants

class MainWindow(private val controller: MainWindowController, stage: Stage) {

    init {
        iniciateWindow(stage)
    }

    private fun iniciateWindow(stage: Stage) {
        // componente para desenho
        val canvas = Canvas()

        // componente para desenhar graficos
        val context = canvas.graphicsContext2D

        // componente para os botões
        val buttons = includeButtons(context)
        controller.bindWidth(buttons)

        // Eventos de mouse
        canvas.setOnMouseMoved { event -> controller.updateWindowTitleWithCoordinates(stage, event) }
        canvas.setOnMouseExited { controller.updateWindowTitleWithoutCoordinates(stage) }
        canvas.setOnMousePressed { event -> controller.drawForm(context, event) }

        val canvasPane = Pane().apply {
            border = Border(BorderStroke(
                    Constants.DEFAULT_PRIMARY_COLOR,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths(0.0, 0.0, 0.0, 1.5)
            ))
            children.add(canvas)
        }

        // Painel para os componentes
        val pane = HBox(controller.panePadding).apply {
            border = Border(BorderStroke(
                    Constants.DEFAULT_PRIMARY_COLOR,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths(1.5)
            ))
            background = Background(BackgroundFill(
                    Constants.DEFAULT_SECONDARY_COLOR,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            ))
            children.addAll(buttons, canvasPane) // posiciona o componente de desenho
        }
        controller.bindCanvasSize(pane, canvas)

        // cria e insere cena
        val scene = Scene(pane)

        // define e inicia o stage
        stage.run {
            // define titulo da janela
            title = controller.title

            // define largura e altura da janela
            width = 800.0
            height = 500.0

            this.scene = scene
            show()
        }
    }

    private fun includeButtons(context: GraphicsContext) : Pane {
        // Painel para as cores
        val colors = VBox(5.0).apply {
            // color choices
            val primaryColorPicker = ColorPicker()
            val secondaryColorPicker = ColorPicker()
            controller.bindColors(
                    primaryColorPicker.valueProperty(),
                    secondaryColorPicker.valueProperty()
            )
            children.addAll(primaryColorPicker, secondaryColorPicker)
        }

        // Painel para os botões
        return VBox(5.0).apply {
            padding = Insets(10.0)
            minWidth = 200.0
            children.addAll(
                    colors,
                    Button().apply { // desenhar pontos
                        text = "Ponto"
                        setOnAction { controller.selectPoint() }
                    },
                    Button().apply { // desenhar linhas
                        text = "Reta"
                        setOnAction { controller.selectLine() }
                    },
                    Button().apply { // desenhar circulos
                        text = "Círculo"
                        setOnAction { controller.selectCircle() }
                    },
                    Button().apply { // desenhar forma com linhas
                        text = "Outras Formas"
                        setOnAction { controller.selectLineForm(context) }
                    },
                    Button().apply { // limpar canvas
                        text = "Limpar"
                        setOnAction { controller.clearCanvas(context) }
                    }
            )
        }
    }

}
