package model.math

import AppTest
import org.junit.Test

class LineTest : AppTest() {
    // line equation: 2x + 1
    private val line = Line(0, 1, 2, 5)

    @Test
    fun testCalculateSlope() {
        val m = line.calculateSlope()

        assertEquals(2.0, m)
    }

    @Test
    fun testCalculateYInterception() {
        val b = line.calculateYInterception()

        assertEquals(1.0, b)
    }
}

