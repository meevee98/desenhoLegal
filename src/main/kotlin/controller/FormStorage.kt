package controller

import java.util.Stack
import javafx.scene.canvas.GraphicsContext
import model.graphic.Form

object FormStorage {
    var history = Stack<FormStorageMemento>()
    var undoHistory = Stack<FormStorageMemento>()
    var forms = mutableListOf<Form>()

    fun draw(form: Form, gc: GraphicsContext) {
        verifyUndoHistory()
        saveOnHistory()
        forms.add(form)
        redraw(gc)
    }

    fun read(figure: List<Form>) {
        verifyUndoHistory()
        saveOnHistory()
        forms.clear()
        forms.addAll(figure)
    }

    fun clear() {
        verifyUndoHistory()
        saveOnHistory()
        forms.clear()
    }

    fun drawClear(form: Form, gc: GraphicsContext) {
        verifyUndoHistory()
        saveOnHistory()
        forms.clear()
        forms.add(form)
        redraw(gc)
    }

    fun redraw(gc: GraphicsContext) {
        forms.forEach { it.draw(gc) }
    }

    fun undo(): Boolean {
        if (history.isNotEmpty()) {
            saveUndoHistory()
            val memento = history.pop()
            forms.clear()
            forms.addAll(memento.getState())

            return true
        }
        return false
    }

    fun redo(): Boolean {
        if (!undoHistory.empty()) {
            saveOnHistory()
            val memento = undoHistory.pop()
            forms.clear()
            forms.addAll(memento.getState())

            return true
        }
        return false
    }

    private fun saveOnHistory() {
        val state = forms.toList()
        history.push(FormStorageMemento(state))
    }

    private fun saveUndoHistory() {
        val state = forms.toList()
        undoHistory.push(FormStorageMemento(state))
    }

    private fun verifyUndoHistory() {
        if (!undoHistory.empty()) {
            undoHistory.clear()
        }
    }


}
