package controller

import model.graphic.Form

class FormStorageMemento(private val state: List<Form>) {

    fun getState(): List<Form> {
        return state
    }
}
