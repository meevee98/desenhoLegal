package TestaPontoGr.primitivos;
 
public class Circulo{
    private Ponto centro;
    private double raio;

    /**
     * Circulo Constructor
     * Constroi um circulo na posicao xc, yc com raio raio 
     * 
     * @param xc int, coordenada x do centro
     * @param yc int, coordenada y do centro
     * @param raio double, raio do circulo
     */
    public Circulo(int xc, int yc, double raio){
        setCentro(new Ponto(xc, yc));
        setRaio(raio);  
    }

    /**
     * Circulo Constructor
     * Constroi um circulo na posicao xc, yc com raio raio 
     * 
     * @param xc double, coordenada x do centro
     * @param yc double, coordenada y do centro
     * @param raio double, raio do circulo
     */
    public Circulo(double xc, double yc, double raio){
        setCentro(new Ponto(xc, yc));
        setRaio(raio);  
    }

    /**
     * Circulo Constructor
     * Constroi um circulo na posicao x, y com raio r 
     *
     * @param centro Ponto, coordenada do centro
     * @param raio double, raio do circulo
     */
    public Circulo(Ponto centro, double raio){
        setCentro(new Ponto(centro));
        setRaio(raio);  
    }


    /**
     * Method setRaio
     *
     * @param raio double, raio do circulo
     */
    private void setRaio(double raio){
        this.raio = raio;
    }

    /**
     * Method setCentro
     *
     * @param centro Ponto, centro do circulo
     */
    private void setCentro(Ponto centro){
        this.centro = centro;
    }

    /**
     * Method getCentro
     *
     * @return Ponto, centro do circulo
     */
    public Ponto getCentro(){
        return this.centro;
    }

    /**
     * Method getRaio
     *
     * @return double, raio do circulo
     */
    public double getRaio(){
        return this.raio;
    }

    /**
     * Method calcularArea
     *
     * @return area do circulo
     */
    public double calcularArea() {
        return Math.PI*Math.pow(getRaio(), 2);
    }
    
    /**
     * Method calcularPerimetro
     *
     * @return perimetro do circulo
     */
    public double calcularPerimetro() {
        return 2.0*Math.PI*getRaio();
    }
    
    /**
     * Method toString
     *
     * @return String, informacoes do circulo
     */
    public String toString(){
        return ("Circulo com centro = " + getCentro().toString() + " e Raio = " + getRaio());
    }
}
