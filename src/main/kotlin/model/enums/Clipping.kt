package model.enums

import model.math.Point

object Clipping {
    val CENTER = 0b0000

    val LEFT = 0b0001
    val RIGHT = 0b0010
    val BOTTOM = 0b0100
    val TOP = 0b1000

    val TOP_LEFT = TOP.or(LEFT)
    val TOP_RIGHT = TOP.or(RIGHT)
    val BOTTOM_LEFT = BOTTOM.or(LEFT)
    val BOTTOM_RIGHT = BOTTOM.or(RIGHT)

    fun classify(p: Point, minX: Double, minY: Double, maxX: Double, maxY: Double): Int {
        return when {
            p.x < minX && p.y < minY -> TOP_LEFT
            p.x < minX && p.y > maxY -> BOTTOM_LEFT
            p.x > maxX && p.y < minY -> TOP_RIGHT
            p.x > maxX && p.y > maxY -> BOTTOM_RIGHT
            p.x < minX -> LEFT
            p.x > maxX -> RIGHT
            p.y < minY -> TOP
            p.y > maxX -> BOTTOM
            else -> CENTER
        }
    }

    fun compare(code: Int, reference: Int): Boolean {
        return code.and(reference) != CENTER
    }
}