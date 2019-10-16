package view

import controller.MainWindowController
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.ColorPicker
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
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
        
        // Eventos de mouse
        canvas.setOnMouseMoved { event -> controller.updateWindowTitleWithCoordinates(stage, event) }
        canvas.setOnMouseExited { controller.updateWindowTitleWithoutCoordinates(stage) }
        canvas.setOnMousePressed { event -> controller.drawForm(context, event) }

        val canvasPane = Pane().apply {
            border = Border(BorderStroke(
                    Constants.DEFAULT_SEPARATOR_COLOR,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths(0.0, 0.0, 0.0, 2.0)
            ))
            children.add(canvas)
        }
        controller.canvas = canvas

        val miniWindow = ImageView().apply {
            fitWidth = 200.0
            fitHeight = 200.0
            imageProperty().bind(controller.canvasSnapshot)
            isPreserveRatio = true
        }

        val menuBar = MenuBar().apply {
            menus.addAll(
                    Menu("Salvar")
            )
        }

        // Painel para os componentes
        val pane = GridPane().apply {
            rowConstraints.addAll(
                    RowConstraints(),
                    RowConstraints().apply {

                        vgrow = Priority.ALWAYS
                    },
                    RowConstraints()
            )
            columnConstraints.addAll(
                    ColumnConstraints(),
                    ColumnConstraints().apply {
                        hgrow = Priority.ALWAYS
                    }
            )

            add(buttons, 0, 1)
            add(miniWindow, 0, 2)
            add(canvasPane, 1, 1, 1 , 2)
            canvasPane.also {
                val row = GridPane.getRowIndex(it)
                val column = GridPane.getColumnIndex(it)
                GridPane.setMargin(it, Insets(0.0, 0.0, 0.0, controller.panePadding))

                it.maxHeightProperty().bind(rowConstraints[row].maxHeightProperty())
                it.maxWidthProperty().bind(columnConstraints[column].maxWidthProperty())
            }


            add(menuBar, 0, 0, 2, 1)
        }

        // cria e insere cena
        val scene = Scene(pane)
        setShortcuts(scene, context)
        controller.bindCanvasSize(scene)

        // define e inicia o stage
        stage.run {
            // define titulo da janela
            title = controller.title

            // define largura e altura da janela
            width = 800.0
            height = 700.0

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
                    Button().apply { // desenhar circulos
                        text = "Polígono"
                        setOnAction { controller.selectPolygon() }
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
