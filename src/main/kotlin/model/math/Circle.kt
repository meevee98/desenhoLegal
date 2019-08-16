package model.math

import kotlin.math.PI
import kotlin.math.pow

/**
 * Representa um circulo com ponto no centro e seu raio.
 *
 * @author Mirella de Medeiros
 *
 */
open class Circle {

    val center: Point
    val radius: Double

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

    // endregion

    override fun toString(): String {
        return "Circulo com centro = $center e Raio = $radius"
    }

}