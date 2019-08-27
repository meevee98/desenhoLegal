package gui;

import controller.Draw;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.enums.BasicForm;
import model.graphic.GraphicPoint;

public class ApplicationGUI {
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

    public ApplicationGUI(Stage stage) {
        iniciateObservables();
        // define titulo da janela
        stage.setTitle(title);

        // define largura e altura da janela
        stage.setWidth(500);
        stage.setHeight(500);

        // Painel para os componentes
        VBox pane = new VBox(panePadding);

        // componente para desenho
        Canvas canvas = new Canvas();

        // componente para desenhar graficos

        GraphicsContext gc;
        gc = canvas.getGraphicsContext2D();

        // componente para os botões
        HBox buttons = new HBox(5);
        includeButtons(buttons, gc);
        diffHeight.bind(buttons.heightProperty().add(panePadding));

        // Eventos de mouse
        // trata mouseMoved
        canvas.setOnMouseMoved(event -> {
            stage.setTitle(title + " (" + (int)event.getX() + ", " + (int)event.getY() + ")" );
        });

        canvas.setOnMouseExited(event -> stage.setTitle(title));

        // trata mousePressed
        canvas.setOnMousePressed(event -> {
            int x, y;

            if (event.getButton() == MouseButton.PRIMARY) {
                x = (int)event.getX();
                y = (int)event.getY();

                // desenha ponto na posicao clicada com nome padrão
                Draw.Handler.drawForm(gc, formaAtual, x, y, drawDiameter, primaryColor, null);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                x = (int)event.getX();
                y = (int)event.getY();

                // desenha ponto na posicao clicada com as coordenadas
                Draw.Handler.drawForm(gc, formaAtual, x, y, drawDiameter, secondaryColor, null);
            }
        });

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
        point.setOnAction(click -> formaAtual = BasicForm.POINT);

        // desenhar linhas
        Button line = new Button("Reta");
        line.setOnAction(click -> {
            formaAtual = BasicForm.LINE;
            Draw.Handler.reset();
        });

        // desenhar circulos
        Button circle = new Button("Círculo");
        circle.setOnAction(click -> {
            formaAtual = BasicForm.CIRCLE;
            Draw.Handler.reset();
        });

        // desenhar outras formas
        Button drawForm = new Button("Outras Formas");
        drawForm.setOnAction(click -> {
            drawFormDialog(gc);
        });

        // limpar canvas
        Button limpar = new Button("Limpar");
        limpar.setOnAction(click -> clean(gc) );

        VBox colors = new VBox(5);
        // primary color choice
        ColorPicker primaryColorPicker = new ColorPicker(primaryColor);
        primaryColorPicker.setOnAction(t -> primaryColor = primaryColorPicker.getValue());

        // secondary color choice
        ColorPicker secondaryColorPicker = new ColorPicker(secondaryColor);
        secondaryColorPicker.setOnAction(t -> secondaryColor = secondaryColorPicker.getValue());

        colors.getChildren().addAll(primaryColorPicker, secondaryColorPicker);
        colors.setMinHeight(60);

        buttons.getChildren().addAll(point, line, circle, drawForm, limpar, colors);
    }

    private void clean(GraphicsContext gc) {
        gc.clearRect(0, 0, canvasWidth.get(), canvasHeight.get());
        Draw.Handler.reset();
    }

    private void drawFormDialog(GraphicsContext gc) {
        TextInputDialog drawFormDialog = new TextInputDialog();

        drawFormDialog.setTitle("Desenhar Forma");
        drawFormDialog.setHeaderText("Insira o número de divisões");
        drawFormDialog.setContentText("Divisões:");

        drawFormDialog.showAndWait()
                .filter(div -> {
                    try {
                        return Integer.parseInt(div) > 0;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .ifPresent(div -> {
                    clean(gc);
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
