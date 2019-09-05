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
        controller.bindHeight(buttons)

        // Eventos de mouse
        canvas.setOnMouseMoved { event -> controller.updateWindowTitleWithCoordinates(stage, event) }
        canvas.setOnMouseExited { controller.updateWindowTitleWithoutCoordinates(stage) }
        canvas.setOnMousePressed { event -> controller.drawForm(context, event) }

        // Painel para os componentes
        val pane = VBox(controller.panePadding).apply {
            background = Background(BackgroundFill(
                    Constants.DEFAULT_SECONDARY_COLOR,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            ))
            children.addAll(buttons, canvas) // posiciona o componente de desenho
        }
        controller.bindCanvasSize(pane, canvas)

        // cria e insere cena
        val scene = Scene(pane)

        // define e inicia o stage
        stage.run {
            // define titulo da janela
            title = controller.title

            // define largura e altura da janela
            width = 500.0
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
            minHeight = 60.0
        }

        // Painel para os botões
        return HBox(5.0).apply {
            children.addAll(
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
                    },
                    colors
            )
        }
    }

}
