package TestaPontoGr.primitivos;

import java.awt.*;

public class CirculoGr extends Circulo{
    private Color corCirculo;
    private String nomeCirculo;
    private int espessura;

    /**
     * @param x
     * @param y
     * @param raio
     * @param cor
     * @param str
     */
    public CirculoGr(int x, int y, double raio, Color cor, String str, int esp){
        super(x, y, raio);
        setCorCirculo(cor);
        setNomeCirculo(str);
        setEspessura(esp);
    }
    
    /**
     * @param c
     * @param raio
     * @param cor
     * @param str
     */
    public CirculoGr(PontoGr c, double raio, Color cor, String str, int esp){
        super(c, raio);
        setCorCirculo(cor);
        setNomeCirculo(str);
        setEspessura(esp);
    }

    /**
     * 
     */
    public CirculoGr(){
        super(new PontoGr(0, 0), 0.0);
        setCorCirculo(Color.black);
        setEspessura(0);
    }

    /**
     * @return the corCirculo
     */
    public Color getCorCirculo() {
        return corCirculo;
    }

    /**
     * @param corCirculo the corCirculo to set
     */
    public void setCorCirculo(Color corCirculo) {
        this.corCirculo = corCirculo;
    }

    /**
     * @return the nomeCirculo
     */
    public String getNomeCirculo() {
        return nomeCirculo;
    }

    /**
     * @param nomeCirculo the nomeCirculo to set
     */
    public void setNomeCirculo(String nomeCirculo) {
        this.nomeCirculo = nomeCirculo;
    }

    /**
     * @return the espessura
     */
    public int getEspessura() {
        return espessura;
    }

    /**
     * @param espessura the espessura to set
     */
    public void setEspessura(int espessura) {
        this.espessura = espessura;
    }

    /**
     * @param g
     */
    void desenharCirculoPolar(Graphics g){
        g.setColor(Color.black);
        g.drawString(getNomeCirculo(),(int)getCentro().getX()+5,(int)getCentro().getY());
        g.setColor(getCorCirculo());
        desenharCirculo((int)getCentro().getX(),(int)getCentro().getY(),(int)getRaio(),g);
    }


    /**
     * @param cx
     * @param cy
     * @param raio
     * @param g
     */
    void desenharCirculo(int cx, int cy, int raio, Graphics g){
    }

    /**
     * @param g
     */
    void desenharCirculoMidPoint(Graphics g){
        g.setColor(Color.black);
        g.drawString(getNomeCirculo(),(int)getCentro().getX()+5,(int)getCentro().getY());
        g.setColor(getCorCirculo());
        desenharCirculoMidPoint((int)getCentro().getX()+40,(int)getCentro().getY(),(int)getRaio(),g);
    }
    /**
     * @param cx
     * @param cy
     * @param raio
     * @param g
     */
    void desenharCirculoMidPoint(int cx, int cy, int raio, Graphics g) {
   }

    //desenha os pontos passados pelo Bresenham para cada 1/8 do circulo
    /**
     * @param x0
     * @param y0
     * @param x
     * @param y
     * @param g
     */
    private void desenharPontos(int x0, int y0, int x, int y, Graphics g) {
        PontoGr p = new PontoGr(x0+x, y0+y, getCorCirculo());
        p.desenharPonto(g);
        p = new PontoGr(x0+y, y0+x, getCorCirculo());
        p.desenharPonto(g);
        p = new PontoGr(x0+y, y0-x, getCorCirculo());
        p.desenharPonto(g);
        p = new PontoGr(x0+x, y0-y, getCorCirculo());
        p.desenharPonto(g);
        p = new PontoGr(x0-x, y0-y, getCorCirculo());
        p.desenharPonto(g);
        p = new PontoGr(x0-y, y0-x, getCorCirculo());
        p.desenharPonto(g);
        p = new PontoGr(x0-y, y0+x, getCorCirculo());
        p.desenharPonto(g);
        p = new PontoGr(x0-x, y0+y, getCorCirculo());
        p.desenharPonto(g);
    }
    private void desenharCirculoOval(Graphics g){
    }
    public static void desenhar(Graphics g, int x, int y, double raio, Color cor, String str, int esp, AlgoritmosCirculos alg) {
        CirculoGr c; 

        // Cria um ponto
        c = new CirculoGr (x, y, raio, cor, str, esp);

        // Desenha o ponto
        c.desenharCirculo(g, alg);
    }

    public static void desenhar(Graphics g, double x1, double y1, double raio, Color cor, String str, int esp, AlgoritmosCirculos alg) {
        desenhar(g, (int)x1, (int) y1, raio, cor, str, esp, alg);
    }

    public void desenharCirculo(Graphics g, AlgoritmosCirculos alg) {
        if (alg == AlgoritmosCirculos.POLAR) {
            desenharCirculoPolar(g);
        } else if (alg == AlgoritmosCirculos.MIDPOINT) {
            desenharCirculoMidPoint(g);
        } else if (alg == AlgoritmosCirculos.GRAPHICS) {
            desenharCirculoOval(g);
        }
    }
}
