package gui;

import controller.Click;
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

    public ApplicationGUI(Stage stage) {

        // define titulo da janela
        stage.setTitle(title);

        // define largura e altura da janela
        // sem definir, é ajustado conforme o tamanho dos componentes
//        stage.setWidth(500);
//        stage.setHeight(500);

        // Painel para os componentes
        BorderPane pane = new BorderPane();

        // componente para os botões
        HBox buttons = new HBox(5);
        includeButtons(buttons);

        // componente para desenho
        Canvas canvas = new Canvas(500, 500);


        // componente para desenhar graficos

        GraphicsContext gc;
        gc = canvas.getGraphicsContext2D();

        // Eventos de mouse
        // trata mouseMoved
        canvas.setOnMouseMoved(event -> {
            stage.setTitle(title + " (" + (int)event.getX() + ", " + (int)event.getY() + ")" );
        });

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
        pane.setTop(buttons);
        pane.setCenter(canvas); // posiciona o componente de desenho

        // cria e insere cena
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    private void includeButtons(HBox buttons) {
        Button point = new Button("Ponto");
        point.setOnAction(click -> formaAtual = BasicForm.POINT);

        Button line = new Button("Reta");
        line.setOnAction(click -> formaAtual = BasicForm.LINE);

        Button circle = new Button("Círculo");
        circle.setOnAction(click -> formaAtual = BasicForm.CIRCLE);

        buttons.getChildren().addAll(point, line, circle);
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
