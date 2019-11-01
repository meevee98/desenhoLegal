package model.controller

import AppTest
import controller.FormStorage
import model.graphic.GraphicLine
import org.junit.Test

class FormStorageTest : AppTest() {

    @Test
    fun testUndoSuccess() {
        val form = GraphicLine(0.0, 3.0, 8.0, 12.0)

        FormStorage.history.clear()
        val result = FormStorage.undo()

        assertEquals(FormStorage.forms.size, 0)
        assertEquals(result, false)
    }

    @Test
    fun testUndoFail() {
        val form = GraphicLine(0.0, 3.0, 8.0, 12.0)

        FormStorage.clear()
        val result = FormStorage.undo()

        assertEquals(FormStorage.forms.size, 0)
        assertEquals(result, true)
    }

    @Test
    fun testRedoSuccess() {
        val form = GraphicLine(0.0, 3.0, 8.0, 12.0)

        FormStorage.clear()
        FormStorage.undo()
        val result = FormStorage.redo()

        assertEquals(FormStorage.forms.size, 0)
        assertEquals(result, true)
    }

    @Test
    fun testRedoFail() {
        val form = GraphicLine(0.0, 3.0, 8.0, 12.0)

        FormStorage.clear()
        val result = FormStorage.redo()

        assertEquals(FormStorage.forms.size, 0)
        assertEquals(result, false)
    }
}