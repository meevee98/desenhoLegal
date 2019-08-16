package model.math

import AppTest
import org.junit.Test
import kotlin.math.PI

class CircleTest : AppTest() {
    // circle equation: x^2 + y^2 = 1
    private val circle = Circle(0, 0, 1.0)

    @Test
    fun testCalculateArea() {
        val area = circle.calculateArea()

        assertEquals(PI, area)
    }

    @Test
    fun testCalculatePerimeter() {
        val expected = 2 * PI
        val perimeter = circle.calculatePerimeter()

        assertEquals(expected, perimeter)
    }
}