package gui;

import controller.Draw;
import controller.MainWindowController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.enums.BasicForm;

public class MainWindow {
    private MainWindowController controller;

    // TODO: Guardar as constantes em outro lugar
    private final String title = "Bora Desenhar";
    private BasicForm formaAtual = BasicForm.POINT;
    private int drawDiameter = 4;
    private int panePadding = 10;

    private Color primaryColor = Color.BLACK;
    private Color secondaryColor = Color.WHITE;

    private DoubleProperty canvasWidth;
    private DoubleProperty canvasHeight;
    private DoubleProperty diffHeight;

    private void iniciateObservables() {
        canvasWidth = new SimpleDoubleProperty(0.0);
        canvasHeight = new SimpleDoubleProperty(0.0);
        diffHeight = new SimpleDoubleProperty(0.0);
    }

    public MainWindow(MainWindowController controller, Stage stage) {
        iniciateObservables();

        this.controller = controller;
        iniciateWindow(stage);
    }

    public void iniciateWindow(Stage stage) {
        // define titulo da janela
        stage.setTitle(title);

        // define largura e altura da janela
        stage.setWidth(500);
        stage.setHeight(500);

        // Painel para os componentes
        VBox pane = new VBox(panePadding);

        // componente para desenho
        Canvas canvas = new Canvas();
//        controller.context = canvas.getGraphicsContext2D();

        // componente para desenhar graficos

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // componente para os botões
        HBox buttons = new HBox(5);
        includeButtons(buttons, gc);
        diffHeight.bind(buttons.heightProperty().add(panePadding));

        // Eventos de mouse
        canvas.setOnMouseMoved(event -> controller.updateWindowTitleWithCoordinates(stage, event));
        canvas.setOnMouseExited(event -> controller.updateWindowTitleWithoutCoordinates(stage));
        canvas.setOnMousePressed(event -> controller.drawForm(gc, event));

        // atributos do painel
        pane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.getChildren().addAll(buttons, canvas); // posiciona o componente de desenho
        letResizeCanvas(pane, canvas);

        // cria e insere cena
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void letResizeCanvas(Pane pane, Canvas canvas) {
        // inicializa variaveis observables
        canvas.widthProperty().bindBidirectional(canvasWidth);
        canvas.heightProperty().bindBidirectional(canvasHeight);

        // Conecta o tamanho do canvas ao tamanho do painel
        canvas.widthProperty().bind(
                pane.widthProperty());
        canvas.heightProperty().bind(
                pane.heightProperty().subtract(diffHeight));
    }

    private void includeButtons(HBox buttons, GraphicsContext gc) {
        // desenhar pontos
        Button point = new Button("Ponto");
        point.setOnAction(click -> controller.selectPoint());

        // desenhar linhas
        Button line = new Button("Reta");
        line.setOnAction(click -> controller.selectLine());

        // desenhar circulos
        Button circle = new Button("Círculo");
        circle.setOnAction(click -> controller.selectCircle());

        // desenhar outras formas
        Button drawForm = new Button("Outras Formas");
        drawForm.setOnAction(click -> drawFormDialog(gc)); // TODO: passar OnActionListener para o controller

        // limpar canvas
        Button clear = new Button("Limpar");
        clear.setOnAction(click -> clear(gc) ); // TODO: passar OnActionListener para o controller


        // color choices
        ColorPicker primaryColorPicker = new ColorPicker();
        ColorPicker secondaryColorPicker = new ColorPicker();
        controller.bindColors(primaryColorPicker, secondaryColorPicker);

        VBox colors = new VBox(5);
        colors.getChildren().addAll(primaryColorPicker, secondaryColorPicker);
        colors.setMinHeight(60);

        buttons.getChildren().addAll(point, line, circle, drawForm, clear, colors);
    }

    private void clear(GraphicsContext gc) {
        gc.clearRect(0, 0, canvasWidth.get(), canvasHeight.get());
        controller.resetDraw();
    }

    private void drawFormDialog(GraphicsContext gc) {
        TextInputDialog drawFormDialog = new TextInputDialog();

        drawFormDialog.setTitle("Desenhar Forma");
        drawFormDialog.setHeaderText("Insira o número de divisões");
        drawFormDialog.setContentText("Divisões:");

        drawFormDialog.showAndWait()
                .filter(input -> controller.isIntGreaterThanZero(input))
                .ifPresent(div -> {
                    clear(gc);
                    Draw.Handler.drawLineForm(
                            gc,
                            canvasWidth.get(),
                            canvasHeight.get(),
                            Integer.parseInt(div),
                            1,
                            primaryColor);
                });
    }

}
