package model.math

import AppTest
import org.junit.Test

class PointTest : AppTest() {
    private val point = Point(0, 0)

    @Test
    fun testCalculateDistance() {
        val distanceP0 = point.calculateDistance(Point(point))
        val distanceP1 = point.calculateDistance(Point(0, 1))
        val distanceP2 = point.calculateDistance(Point(3, 4))
        val distanceP3 = point.calculateDistance(Point(1, 0))

        assertEquals(0.0, distanceP0)
        assertEquals(1.0, distanceP1)
        assertEquals(5.0, distanceP2)
        assertEquals(1.0, distanceP3)

    }
}

