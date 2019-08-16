package TestaPontoGr.primitivos;


/**
 * Write a description of class TestaCirculo here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TestaCirculo{
    public static void main(String args[]) {
        Circulo c = new Circulo(2, 2, 10);
        System.out.println(c);
        System.out.println("Area do circulo: "+ c.calcularArea());
        System.out.println("Perimetro do circulo: "+ c.calcularPerimetro());
    }
}
