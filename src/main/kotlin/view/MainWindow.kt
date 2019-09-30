package view

import controller.MainWindowController
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.ColorPicker
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCombination
import javafx.scene.layout.*
import javafx.stage.Stage
import model.constants.Constants

class MainWindow(private val controller: MainWindowController, stage: Stage) {

    init {
        iniciateWindow(stage)
    }

    private fun iniciateWindow(stage: Stage) {
        // componente para desenho
        val canvas = Canvas().apply {
            width = Constants.CANVAS_WIDTH
            height = Constants.CANVAS_HEIGHT
        }

        // componente para desenhar graficos
        val context = canvas.graphicsContext2D

        // componente para os botões
        val buttons = includeButtons(context)
        controller.bindWidth(buttons)
        
        // Eventos de mouse
        canvas.setOnMouseMoved { event -> controller.updateWindowTitleWithCoordinates(stage, event) }
        canvas.setOnMouseExited { controller.updateWindowTitleWithoutCoordinates(stage) }
        canvas.setOnMousePressed { event -> controller.drawForm(context, event) }

        val canvasPane = BorderPane().apply {
            border = Border(BorderStroke(
                    Constants.DEFAULT_SEPARATOR_COLOR,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths(0.0, 0.0, 0.0, 2.0)
            ))
            center = canvas
        }
        controller.canvasPane = canvasPane

        val miniWindow = ImageView().apply {
            fitWidth = 200.0
            fitHeight = 200.0
            imageProperty().bind(controller.canvasSnapshot)
            isPreserveRatio = true
        }

        val gridMenu = GridPane().apply {
            rowConstraints.add(RowConstraints().apply {
                vgrow = Priority.ALWAYS
            })
            columnConstraints.add(ColumnConstraints().apply {
                alignment = Pos.BOTTOM_CENTER
            })
            add(buttons, 0, 0)
            add(miniWindow, 0, 1)

        }

        // Painel para os componentes
        val pane = HBox(Constants.DEFAULT_PANE_PADDING).apply {
            border = Border(BorderStroke(
                    Constants.DEFAULT_SEPARATOR_COLOR,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths(2.0)
            ))
            background = Background(BackgroundFill(
                    Constants.DEFAULT_BACKGROUND_COLOR,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            ))

            children.addAll(gridMenu, canvasPane) // posiciona o componente de desenho
        }
        controller.bindCanvasSize(pane)

        // cria e insere cena
        val scene = Scene(pane)
        setShortcuts(scene, context)

        // define e inicia o stage
        stage.run {
            // define titulo da janela
            title = controller.title

            // define largura e altura da janela
            width = 800.0
            height = 600.0

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
                    Button().apply { // desenhar circulos
                        text = "Retângulo"
                        setOnAction { controller.selectRectangle() }
                    },
                    Button().apply { // desenhar forma com linhas
                        text = "Outras Formas"
                        setOnAction { controller.selectLineForm(context) }
                    },
                    Button().apply { // limpar canvas
                        text = "Limpar"
                        setOnAction { controller.clear(context) }
                    },
                    Button().apply { // desfazer ação
                        text = "Desfazer"
                        setOnAction { controller.undo(context) }
                    },
                    Button().apply { // refazer ação
                        text = "Refazer"
                        setOnAction { controller.redo(context) }
                    },
                    Button().apply { // clipping do canvas
                        text = "Clip"
                        isDisable = true
                        setOnAction { controller.clip(context) }
                    }
            )
        }
    }

    private fun setShortcuts(scene: Scene, context: GraphicsContext) {
        val shortcuts = mapOf(
                KeyCombination.valueOf("Ctrl+Z") to Runnable { controller.undo(context) },
                KeyCombination.valueOf("Ctrl+Y") to Runnable { controller.redo(context) },
                KeyCombination.valueOf("Ctrl+Shift+Z") to Runnable { controller.redo(context) },
                KeyCombination.valueOf("Ctrl+Delete") to Runnable { controller.clear(context) }
        )

        scene.accelerators.putAll(shortcuts)
    }
}
