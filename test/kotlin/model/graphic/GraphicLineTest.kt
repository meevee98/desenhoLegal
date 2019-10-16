package model.graphic

import AppTest
import org.junit.Test

class GraphicLineTest : AppTest() {
    private val line = GraphicLine(0.0, 1.0, 6.0, 7.0)

    @Test
    fun testClip() {
        val rec = GraphicRectangle(1, 1, 5, 5)
        line.clip(rec)

        assertEquals(1.0, line.p1.x)
        assertEquals(2.0, line.p1.y)
        assertEquals(4.0, line.p2.x)
        assertEquals(5.0, line.p2.y)
    }

    @Test
    fun testCalculateYInterception() {
        val b = line.calculateYInterception()

        assertEquals(1.0, b)
    }
}

