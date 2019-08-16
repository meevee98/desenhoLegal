package TestaPontoGr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * Escreva a descrição da classe PainelDesenho aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class PainelDesenho extends JPanel implements MouseListener, MouseMotionListener {
    JLabel msg;
    TestaPontoGr.TiposPrimitivos tipo;
    int divisoes;
    int xMouse, yMouse;

    /**
     * COnstrutor para objetos da classe PainelDesenho
     */
    public PainelDesenho(JLabel msg, TiposPrimitivos tipo)
    {
        this.tipo = tipo;
        this.msg = msg;
        //       this.setBackground(Color.black);
        this.addMouseListener(this); 
        this.addMouseMotionListener(this);

    }
    public void setTipo(TiposPrimitivos tipo){
        this.tipo = tipo;
    }

    public TiposPrimitivos getTipo(){
        return this.tipo;
    }

    public void paintComponent(Graphics g) {   
        //super.paintComponent(g);   
        //desenharPrimitivos(g);
        FiguraPontos.desenharPonto(g, xMouse, yMouse, Color.red, 10);

    }

    // Capturando os Eventos com o mouse
    public void mousePressed(MouseEvent e) { 
        xMouse = e.getX();
        yMouse = e.getY();
        repaint();
    }     

    public void mouseReleased(MouseEvent e) { 
    }           

    public void mouseClicked(MouseEvent e) {
        //this.msg.setText("CLICOU: "+e.getButton());
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {

        this.msg.setText("("+e.getX() + ", " + e.getY() + ")");
        //System.out.println("("+e.getX() + ", " + e.getY() + ")");

    }

    public void desenharPrimitivos(Graphics g){
        if (tipo==TiposPrimitivos.PONTOS){
            FiguraPontos.desenharPontos(g, 50, 8);

        }

        if (tipo==TiposPrimitivos.RETAS){
            //FiguraRetas.desenharRetas(g, getDivisoes(), 80, 80, 500);
        }
        if (tipo==TiposPrimitivos.CIRCULOS){
            //FiguraCirculos.desenharCirculos(g, 1);
        }
        if (tipo==TiposPrimitivos.LETRAS){
            //FiguraVogais.desenharVogais(g, 5, 3, 90,1);
        }
    }

}
