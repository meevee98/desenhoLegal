package gui;

import controller.Click;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
        VBox pane = new VBox();

        // componente para desenho
        Canvas canvas = new Canvas();

        // componente para desenhar graficos

        GraphicsContext gc;
        gc = canvas.getGraphicsContext2D();

        // componente para os botões
        HBox buttons = new HBox(5);
        includeButtons(buttons, gc);
        diffHeight.bind(buttons.heightProperty());

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
                Click.Handler.drawForm(gc, formaAtual, x, y, 4, Color.CADETBLUE, null);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                x = (int)event.getX();
                y = (int)event.getY();

                // desenha ponto na posicao clicada com as coordenadas
                Click.Handler.drawForm(gc, formaAtual, x, y, 4, Color.LIGHTSALMON, "("+ x + ", " + y +")");
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
        Button point = new Button("Ponto");
        point.setOnAction(click -> formaAtual = BasicForm.POINT);

        Button line = new Button("Reta");
        line.setOnAction(click -> formaAtual = BasicForm.LINE);

        Button circle = new Button("Círculo");
        circle.setOnAction(click -> formaAtual = BasicForm.CIRCLE);

        Button limpar = new Button("Limpar");
        limpar.setOnAction(click->{
            gc.clearRect(0, 0, canvasWidth.get(), canvasHeight.get());
        });

        buttons.getChildren().addAll(point, line, circle,limpar);
    }


    /**
     * Desenha um ponto grafico
     *
     * @param g contexto grafico
     * @param x posicao x
     * @param y posicao y
     * @param diametro diametro do ponto
     * @param nome nome do ponto
     * @param cor cor do ponto
     */
    public void drawPoint(GraphicsContext g, int x, int y, int diametro, String nome, Color cor) {
        GraphicPoint p;
        // Cria um ponto
        p = new GraphicPoint(x, y, cor, diametro, nome);

        // Desenha o ponto
        p.drawPoint(g);
    }
}
