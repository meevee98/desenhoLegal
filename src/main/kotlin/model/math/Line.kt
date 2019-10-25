package model.math


/**
 * Representa uma reta atraves de 2 pontos.
 *
 * @author Mirella de Medeiros
 *
 */
open class Line {

    var p1: Point
    var p2: Point

    // region CONSTRUCTORS

    constructor(p1: Point, p2: Point) {
        this.p1 = p1
        this.p2 = p2
    }

    constructor(x1: Double, y1: Double, x2: Double, y2: Double)
        : this (Point(x1, y1), Point(x2, y2))

    constructor(x1: Int, y1: Int, x2: Int, y2: Int)
        : this (Point(x1, y1), Point(x2, y2))

    // endregion

    // region MATH FUNCTIONS

    /**
     * Method calculateSlope
     *
     * @return Retorna o valor do coeficiente angular da reta (m na equação y = mx + b)
     */
    fun calculateSlope(): Double {
        if (p1.x == p2.x) {
            // without this will be division by 0
            return 0.0
        }
        return (p2.y - p1.y) / (p2.x - p1.x)
    }

    /**
     * Method calculateYInterception
     *
     * @return Retorna o valor do coeficiente linear da reta (b na equação y = mx + b)
     */
    fun calculateYInterception(): Double {
        return p1.y - calculateSlope() * p1.x
    }

    companion object {
        /**
         * Method calculateX
         *
         * @param y
         * @param b
         * @param m
         *
         * @return Retorna o valor de x //      x = ( y - b ) / m
         */
        fun calculateX (y : Double, b : Double, m : Double): Double {
            return (y - b) / m
        }

        /**
         * Method calculateX
         *
         * @param x
         * @param b
         * @param m
         *
         * @return Retorna o valor de y //      y = m * x + b
         */
        fun calculateY (x : Double, b : Double, m : Double): Double {
            return m * x + b
        }
    }
    // endregion

    override fun toString(): String {
        return "P1: $p1 P2: $p2\n" +
               "Eq. da reta: y = ${calculateSlope()}*x + ${calculateYInterception()}"
    }

}