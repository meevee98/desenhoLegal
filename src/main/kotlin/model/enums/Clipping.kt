package model.enums

import model.math.Point

class Clipping {
    companion object {
        val CENTER = 0b0000

        val LEFT = 0b0001
        val RIGHT = 0b0010
        val BOTTOM = 0b0100
        val TOP = 0b1000

        val TOP_LEFT = TOP.or(LEFT)
        val TOP_RIGHT = TOP.or(RIGHT)
        val BOTTOM_LEFT = BOTTOM.or(LEFT)
        val BOTTOM_RIGHT = BOTTOM.or(RIGHT)
    }

    fun isInside(p: Point, min: Point, max: Point): Boolean {
        return classify(p, min.x, min.y, max.x, max.y) == CENTER
    }

    fun isFullyInside(p1: Point, p2: Point, min: Point, max: Point): Boolean {
        return isInside(p1, min, max) &&
               isInside(p2, min, max)
    }

    fun isFullyOutside(p1: Point, p2: Point, min: Point, max: Point): Boolean {
        return compare(
                classify(p1, min.x, min.y, max.x, max.y),
                classify(p2, min.x, min.y, max.x, max.y)
        )
    }

    fun compare(p: Point, min: Point, max: Point, reference: Int): Boolean {
        return classify(p, min.x, min.y, max.x, max.y).and(reference) != CENTER
    }

    private fun compare(code: Int, reference: Int): Boolean {
        return code.and(reference) != CENTER
    }

    private fun classify(p: Point, minX: Double, minY: Double, maxX: Double, maxY: Double): Int {
        return when {
            p.x < minX && p.y < minY -> TOP_LEFT
            p.x < minX && p.y > maxY -> BOTTOM_LEFT
            p.x > maxX && p.y < minY -> TOP_RIGHT
            p.x > maxX && p.y > maxY -> BOTTOM_RIGHT
            p.x < minX -> LEFT
            p.x > maxX -> RIGHT
            p.y < minY -> TOP
            p.y > maxY -> BOTTOM
            else -> CENTER
        }
    }
}