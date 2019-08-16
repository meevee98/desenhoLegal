package TestaPontoGr.primitivos;

import java.awt.Color;
import java.awt.Graphics;

public class RetaGr extends Reta{
    private Color corReta;
    private String nomeReta;
    private int espessura;

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param corReta
     * @param nomeReta
     * @param espessura
     */
    public RetaGr(int x1, int y1, int x2, int y2, Color corReta, String nomeReta, int espessura){
        super (x1, y1, x2, y2);
        setCorReta(corReta);
        setNomeReta(nomeReta);
        setEspessura(espessura);
    }    

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param corReta
     * @param espessura
     */
    public RetaGr(int x1, int y1, int x2, int y2, Color corReta, int espessura){
        super (x1, y1, x2, y2);
        setCorReta(corReta);
        setNomeReta("");
        setEspessura(espessura);
    }   

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param espessura
     */
    public RetaGr(int x1, int y1, int x2, int y2, int espessura){
        super (x1, y1, x2, y2);
        setCorReta(Color.black);
        setNomeReta("");
        setEspessura(espessura);
    }   

    /**
     * @param p1
     * @param p2
     * @param espessura
     */
    public RetaGr(PontoGr p1, PontoGr p2, int espessura){
        super(p1, p2);
        setCorReta(Color.black);
        setNomeReta("");
        setEspessura(espessura);
    }    

    /**
     * @param p1
     * @param p2
     * @param corRet
     * @param espessura
     */
    public RetaGr(PontoGr p1, PontoGr p2, Color corRet, int espessura){
        super(p1, p2);
        setCorReta(corReta);
        setNomeReta("");
        setEspessura(espessura);
    }    

    /**
     * @param p1
     * @param p2
     * @param corReta
     * @param nomeReta
     * @param espessura
     */
    public RetaGr(PontoGr p1, PontoGr p2, Color corReta, String nomeReta, int espessura){
        super(p1, p2);
        setCorReta(corReta);
        setNomeReta(nomeReta);
        setEspessura(espessura);
    }    

    /**
     * @return the corReta
     */
    public Color getCorReta() {
        return corReta;
    }

    /**
     * @param corReta the corReta to set
     */
    private void setCorReta(Color corReta) {
        this.corReta = corReta;
    }

    /**
     * @return the nomeReta
     */
    public String getNomeReta() {
        return nomeReta;
    }

    /**
     * @param nomeReta the nomeReta to set
     */
    private void setNomeReta(String nomeReta) {
        this.nomeReta = nomeReta;
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
    private void setEspessura(int espessura) {
        this.espessura = espessura;
    }

    /**
     * @param g
     */
    public void desenharRetaEq(Graphics g){

        double b = calcularB();
        double m = calcularM();

        //Caso 1) Caso em que o intervalo em y é maior
        if(Math.abs(getP2().getY()-getP1().getY())>Math.abs(getP2().getX()-getP1().getX())){

            //Caso 1.1)Caso em que y1 > y2
            if(getP1().getY()>getP2().getY()){

                //System.out.println("Intervalo em Y eh maior com y1 > y2 .");
                if (getP1().getX()==getP2().getX()){
                    PontoGr ponto=new PontoGr((int)getP1().getX(),(int)getP1().getY(), getCorReta(), getEspessura());
                    ponto.desenharPonto(g);
                    for(double y=getP2().getY();y<getP1().getY();++y){
                        ponto=new PontoGr((int)(getP1().getX()),(int)y, getCorReta(), getEspessura());
                        ponto.desenharPonto(g);
                    }
                }
                else{
                    PontoGr ponto=new PontoGr((int)getP2().getX(),(int)getP2().getY(), getCorReta(),  getEspessura());
                    ponto.desenharPonto(g);
                    for(double y=getP2().getY();y<getP1().getY();++y){
                        ponto=new PontoGr((int)((y-b)/m),(int)y, getCorReta(), getEspessura());
                        ponto.desenharPonto(g);
                    }
                }

                //Caso 1.2)Caso em que x1 = x2
            }else if(getP1().getX()==getP2().getX()){

                //System.out.println("Intervalo em Y eh maior com Reta vertical .");
                PontoGr ponto=new PontoGr((int)getP1().getX(),(int)getP1().getY(), getCorReta(), getEspessura());
                ponto.desenharPonto(g);
                for(double y=getP1().getY();y<getP2().getY();++y){
                    ponto=new PontoGr((int)(getP1().getX()),(int)y, getCorReta(), getEspessura());
                    ponto.desenharPonto(g);
                }
                //Caso 1.3)Caso em que x1 < x2  
            }else{

                //System.out.println("Intervalo em Y eh maior com x1 < x2 .");
                PontoGr ponto=new PontoGr((int)getP1().getX(),(int)getP1().getY(), getCorReta(), getEspessura());
                ponto.desenharPonto(g);
                for(double y=getP1().getY();y<getP2().getY();++y){
                    ponto=new PontoGr((int)((y-b)/m),(int)y, getCorReta(), getEspessura());
                    ponto.desenharPonto(g);
                }

            }

            //Caso 2)Caso em que o intervalo em x é maior
        }else{

            //Caso 2.1)Caso em que x1 > x2
            if(getP1().getX()>getP2().getX()){

                //System.out.println("Intervalo em X eh maior com x1 > x2 .");
                PontoGr ponto=new PontoGr((int)getP2().getX(),(int)getP2().getY(), getCorReta(), getEspessura());
                ponto.desenharPonto(g);
                for(double x=getP2().getX();x<getP1().getX();++x){
                    ponto=new PontoGr((int)x,(int)(b+m*x), getCorReta(), getEspessura());
                    ponto.desenharPonto(g);
                }

                //Caso 2.2)Caso em que x1 = x2  
            }else if(getP1().getX()==getP2().getX()){

                //System.out.println("Intervalo em X eh maior com Reta Vertical .");
                PontoGr ponto=new PontoGr((int)getP1().getX(),(int)getP1().getY(), getCorReta(), getEspessura());
                ponto.desenharPonto(g);
                for(double x=getP1().getX();x<getP2().getX();++x){
                    ponto=new PontoGr((int)(getP1().getX()),(int)(b+m*x), getCorReta(), getEspessura());
                    ponto.desenharPonto(g);
                }

                //Caso 2.3)Caso em que x1 < x2
            }else{

                //System.out.println("Intervalo em X eh maior com x1 < x2 .");
                PontoGr ponto=new PontoGr((int)getP1().getX(),(int)getP1().getY(), getCorReta(), getEspessura());
                ponto.desenharPonto(g);
                for(double x=getP1().getX();x<getP2().getX();++x){
                    ponto=new PontoGr((int)x,(int)(b+m*x), getCorReta(), getEspessura());
                    ponto.desenharPonto(g);
                }
            }
        }
    }

    /**
     * @param g
     */
    private void desenharRetaDDA(Graphics g){
    }

    /**
     * @param g
     */
    private void desenharRetaMidPoint(Graphics g){
    }

    /**
     * @param g
     */
    private void desenharRetaDrawLine(Graphics g){
    }

    /**
     * Desenha reta grafica 
     * 
     * @param g contexto grafico
     * @param x1 x de P1
     * @param y1 y de P1
     * @param x2 x de P2
     * @param y2 y de P2
     * @param nome nome da reta
     * @param cor cor do ponto
     * @param esp espessura da reta
     */
    public static void desenhar(Graphics g, int x1, int y1, int x2, int y2, String nome, Color cor, int esp, AlgoritmosRetas alg) {
        RetaGr r; 

        // Cria um ponto
        r = new RetaGr(x1, y1, x2, y2, cor, nome, esp);

        // Desenha o ponto
        r.desenharReta(g, alg);
    }

    /**
     * @param g
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param nome
     * @param cor
     * @param esp
     * @param alg
     */
    public static void desenhar(Graphics g, double x1, double y1, double x2, double y2, String nome, Color cor, int esp, AlgoritmosRetas alg) {
        desenhar(g, (int)x1, (int) y1, (int) x2, (int) y2, nome, cor, esp, alg);
    }

    /**
     * @param g
     * @param alg
     */
    public void desenharReta(Graphics g, AlgoritmosRetas alg) {
        if(alg == AlgoritmosRetas.EQUACAO) {
            desenharRetaEq(g);
        } else if (alg == AlgoritmosRetas.DDA) {
            desenharRetaDDA(g);
        } else if (alg == AlgoritmosRetas.MIDPOINT) {
            desenharRetaMidPoint(g);
        } else if (alg == AlgoritmosRetas.GRAPHICS) {
            desenharRetaDrawLine(g);
        }
    }
}