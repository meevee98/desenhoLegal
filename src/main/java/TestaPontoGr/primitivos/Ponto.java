package TestaPontoGr.primitivos;
 
/**
 * Representa um ponto geometrico ("matematico") 
 * com coordenadas x e y reais
 * 
 * @author Julio Arakaki
 * 
 */
public class Ponto {
    // Atributos x e y do ponto
    private double x;
    private double y;

    // Construtores
    /**
     * Ponto Constructor
     *
     */
    public Ponto () {
    	this(0.0, 0.0);
    }

    /**
     * Ponto Constructor
     *
     * @param x double, coordenada x
     * @param y double, coordenada y
     */
    public Ponto (double x, double y) {
        setX(x);
        setY(y);
    }

    /**
     * Ponto Constructor
     *
     * @param x int, coordenada x
     * @param y int, coordenada y
     */
    public Ponto (int x, int y) {
    	this((double)x, (double)y);
   }

    /**
     * Ponto Constructor
     *
     * @param p Ponto, coordenadas x e y
     */
    public Ponto (Ponto p) {
    	this(p.getX(), p.getY());
    }

    /**
     * Method setX
     *
     * @param x Valor da coordenada x
     */
    private void setX(double x) {
        this.x = x;
    }

    /**
     * Method setY
     *
     * @param y Valor da coordenada y
     */
    private void setY(double y) {
        this.y = y;
    }

    /**
     * Method getX
     *
     * @return o valor de x
     */
    public double getX() {
        return this.x;
    }

    /**
     * Method getY
     *
     * @return o valor de y
     */
    public double getY() {
        return this.y;
    }

    /**
     * Method calcularDistancia
     *
     * @param p ponto externo
     * @return retorna a distancia ao ponto externo (parametro)
     */
    public double calcularDistancia(Ponto p) {
        return Math.sqrt(Math.pow((p.getY() - getY()),2) +
            Math.pow((p.getX() - getX()),2));
    }
    
    /**
     *
     */
    public String toString(){
        return("(" + getX() + ", " + getY() + ")");
    }
}