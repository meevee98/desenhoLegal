package TestaPontoGr.primitivos;
 
/**
 * Representa uma reta atraves de 2 pontos.
 * 
 * @author Julio Arakaki
 *
 */
public class Reta{
    
    private Ponto p1, p2;
    
    //Construtores
    /**
     * Reta Constructor
     *
     * @param x1 A parameter
     * @param y1 A parameter
     * @param x2 A parameter
     * @param y2 A parameter
     */
    public Reta(int x1, int y1, int x2, int y2){
        
        setP1(new Ponto(x1, y1));
        setP2(new Ponto(x2, y2));
    }    
    
     /**
     * Reta Constructor
     *
     * @param x1 A parameter
     * @param y1 A parameter
     * @param x2 A parameter
     * @param y2 A parameter
     */
    public Reta(double x1, double y1, double x2, double y2){
        
        setP1(new Ponto(x1, y1));
        setP2(new Ponto(x2, y2));
    }    
   /**
     * Reta Constructor
     *
     * @param p1 A parameter
     * @param p2 A parameter
     */
    public Reta(Ponto p1, Ponto p2){
        setP1(new Ponto(p1));
        setP2(new Ponto(p2));
    }    
    
    //Sets
    /**
     * Method setP1
     *
     * @param p1 A parameter
     */
    private void setP1(Ponto p1) {
        this.p1 = p1;
    }
    
    /**
     * Method setP2
     *
     * @param p2 A parameter
     */
    private void setP2(Ponto p2) {
        this.p2 = p2;
    }
    
    
    //Gets
    /**
     * Method getP1
     *
     * @return The return value
     */
    public Ponto getP1() {
        return p1;
    }
    
    /**
     * Method getP2
     *
     * @return The return value
     */
    public Ponto getP2() {
        return p2;
    }
    
    
    /**
     * Method calcularM
     *
     * @return The return value
     */
    public double calcularM(){
        
        double m = (getP2().getY()-getP1().getY())/(getP2().getX()-getP1().getX());
        return m;
    }
    
    //Calcula b da equa��o da reta (y = m*x + b)
    /**
     * Method calcularB
     *
     * @return The return value
     */
    public double calcularB(){
        double m = calcularM();
        double b = getP2().getY()-(m*getP2().getX());
        return b;
    }   
    
    /**
     * Method toString
     *
     * @return The return value
     */
    public String toString(){
        String s = "P1: " + getP1().toString() + " P2: " + getP2().toString();
        s = s + "\nEq. da reta: y = " + calcularM() + "*x + " + calcularB();
        return s;
    }
}