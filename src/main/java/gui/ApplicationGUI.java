package gui;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.graphic.GraphicPoint;

public class ApplicationGUI {
    private final String title = "Bora Desenhar";
    private int indicePonto = 1;

    public ApplicationGUI(Stage stage) {

        // define titulo da janela
        stage.setTitle(title);

        // define largura e altura da janela
        stage.setWidth(500);
        stage.setHeight(500);

        // Painel para os componentes
        BorderPane pane = new BorderPane();

        // componente para desenho
        Canvas canvas = new Canvas(500, 500);

        // componente para desenhar graficos

        GraphicsContext gc;
        gc = canvas.getGraphicsContext2D();

        // Eventos de mouse
        // trata mouseMoved
        canvas.setOnMouseMoved(event -> {
            stage.setTitle(title + " (" + (int)event.getX() + ", " + (int)event.getY() + ")");
        });

        // trata mousePressed
        canvas.setOnMousePressed(event -> {
            int x, y;

            if (event.getButton() == MouseButton.PRIMARY) {
                x = (int)event.getX();
                y = (int)event.getY();
                // desenha ponto na posicao clicada
                drawPoint(gc, x, y, 8, "P" + indicePonto, Color.AZURE);
                indicePonto++;
            } else if (event.getButton() == MouseButton.SECONDARY) {
                x = (int)event.getX();
                y = (int)event.getY();
                // desenha ponto na posicao clicada
                drawPoint(gc, x, y, 8, "("+ x + ", " + y +")", Color.LIGHTSALMON);
            }
        });

        // atributos do painel
        pane.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setCenter(canvas); // posiciona o componente de desenho

        // cria e insere cena
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
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
