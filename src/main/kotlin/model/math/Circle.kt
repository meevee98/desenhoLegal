package model.math

import kotlin.math.*

/**
 * Representa um circulo com ponto no centro e seu raio.
 *
 * @author Mirella de Medeiros
 *
 */
open class Circle {

    var center: Point
    var radius: Double

    // region CONSTRUCTORS

    constructor(center: Point, radius: Double) {
        this.center = center
        this.radius = radius
    }

    constructor(xC: Int, yC: Int, radius: Double)
            : this(Point(xC, yC), radius)

    constructor(xC: Double, yC: Double, radius: Double)
            : this(Point(xC, yC), radius)

    // endregion

    // region MATH FUNCTIONS

    /**
     * Method calculateArea
     *
     * @return area do circulo
     */
    fun calculateArea(): Double {
        return PI * radius.pow(2)
    }

    /**
     * Method calculatePerimeter
     *
     * @return perimetro do circulo
     */
    fun calculatePerimeter(): Double {
        return 2 * PI * radius
    }

    companion object {
        /**
         * Method calculateX
         *
         * @param degree
         * @param radius
         *
         * @return Retorna o valor x em um circulo de centro (0,0), raio = radius e grau = degree
         */
        fun calculateX (degree : Double, radius : Double): Double {
            return sin(degree) * radius
        }

        /**
         * Method calculateY
         *
         * @param x
         * @param radius
         *
         * @return Retorna o valor y em um circulo de centro (0,0) // x^2 + y^2 = r^2
         */
        fun calculateY (x : Double, radius : Double): Double {
            return sqrt(radius.pow(2) - x.pow(2))
        }
    }

    // endregion

    override fun toString(): String {
        return "Circulo com centro = $center e Raio = $radius"
    }

}