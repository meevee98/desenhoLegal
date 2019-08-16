package model.math

import kotlin.math.sqrt
import kotlin.math.pow

/**
 * Representa um ponto geometrico
 * com coordenadas x e y reais
 *
 * @author Mirella de Medeiros
 *
 */
open class Point {

    val x: Double
    val y: Double

    // region CONSTRUCTORS

    constructor(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    constructor(x: Int, y: Int)
        : this(x.toDouble(), y.toDouble())

    constructor(point: Point)
        : this(point.x, point.y)

    // endregion

    // region MATH FUNCTIONS

    /**
     * Method calculateDistance
     *
     * @param point ponto externo
     * @return retorna a distancia entre esse ponto e o ponto externo (parametro)
     */
    fun calculateDistance(point: Point) : Double {
        return sqrt(
            (point.y - this.y).pow(2) +
                (point.x - this.x).pow(2) )
    }

    // endregion

    override fun toString(): String {
        return "($x, $y)"
    }

}