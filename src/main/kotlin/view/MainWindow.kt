package view

import controller.MainWindowController
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCombination
import javafx.scene.layout.*
import javafx.stage.Stage
import model.constants.Constants
import model.enums.BasicForm

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
        val supportCanvas = Canvas().apply {
            width = Constants.CANVAS_WIDTH
            height = Constants.CANVAS_HEIGHT
        }

        // componente para desenhar graficos
        val context = canvas.graphicsContext2D

        // componente para os botões
        val buttons = includeButtons(context)

        val canvasPane = Pane().apply {
            border = Border(BorderStroke(
                    Constants.DEFAULT_SEPARATOR_COLOR,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths(0.0, 0.0, 0.0, 2.0)
            ))
            background = Background(BackgroundFill(
                    Constants.DEFAULT_BACKGROUND_COLOR,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            ))
            children.addAll(canvas, supportCanvas)
            supportCanvas.toFront()
        }
        controller.mainCanvas = canvas
        controller.supportCanvas = supportCanvas

        // Eventos de mouse
        handleMouseEvents(canvasPane, stage, context)

        val miniWindow = BorderPane().apply {
            setPrefSize(200.0, 200.0)
            border = Border(BorderStroke(
                    Constants.DEFAULT_SEPARATOR_COLOR,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths(2.0)
            ))

            center = ImageView().apply {
                fitWidth = 200.0
                fitHeight = 200.0
                imageProperty().bind(controller.canvasSnapshot)
                isPreserveRatio = true
            }
        }

        val menuBar = includeMenu(stage)

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
            miniWindow.also {
                GridPane.setMargin(it, Insets(0.0, 0.0, controller.panePadding, controller.panePadding))
            }


            add(menuBar, 0, 0, 2, 1)
        }

        // cria e insere cena
        val scene = Scene(pane)
        controller.bindCanvasSize(scene)

        // define e inicia o stage
        stage.run {
            // define titulo da janela
            title = controller.title

            // define largura e altura da janela
            width = 800.0
            height = 700.0

            this.scene = scene
            setShortcuts(this, context)
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
                    ComboBox<BasicForm>().apply {
                        itemsProperty().bind(controller.availableForms)
                        valueProperty().bindBidirectional(controller.selectedForm)

                        setOnAction { value?.let { controller.selectForm(it) } }
                    },
                    Button().apply { // desenhar forma com linhas
                        text = "Curva com retas"
                        disableProperty().bind(controller.clippingActive)
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
                    CheckBox().apply { // clipping do canvas
                        text = "Clip"
                        selectedProperty().bindBidirectional(controller.clipping)
                        selectedProperty().addListener { _ -> controller.clip() }
                    }
            )
        }
    }

    private fun includeMenu(stage: Stage): MenuBar {
        return MenuBar().apply {
            menus.addAll(
                    Menu("Arquivo").apply {
                        items.addAll(
                                MenuItem("Abrir (Ctrl + O)").apply {
                                    setOnAction { controller.openFigureFromFile(stage) }
                                },
                                MenuItem("Salvar (Ctrl + S)").apply {
                                    setOnAction { controller.saveFigureOnFile(stage) }
                                }
                        )
                    }
            )
        }
    }

    private fun handleMouseEvents(canvas: Pane, stage: Stage, context: GraphicsContext) {
        canvas.setOnMouseMoved { event -> controller.updateWindowTitleWithCoordinates(stage, event) }
        canvas.setOnMouseExited { controller.updateWindowTitleWithoutCoordinates(stage) }
        canvas.setOnMousePressed { event -> controller.drawForm(context, event) }
    }

    private fun setShortcuts(stage: Stage, context: GraphicsContext) {
        val shortcuts = mapOf(
                KeyCombination.valueOf("Ctrl+O") to Runnable { controller.openFigureFromFile(stage) },
                KeyCombination.valueOf("Ctrl+S") to Runnable { controller.saveFigureOnFile(stage) },
                KeyCombination.valueOf("Ctrl+Z") to Runnable { controller.undo(context) },
                KeyCombination.valueOf("Ctrl+Y") to Runnable { controller.redo(context) },
                KeyCombination.valueOf("Ctrl+Shift+Z") to Runnable { controller.redo(context) },
                KeyCombination.valueOf("Ctrl+Delete") to Runnable { controller.clear(context) }
        )

        stage.scene.accelerators.putAll(shortcuts)
    }
}
