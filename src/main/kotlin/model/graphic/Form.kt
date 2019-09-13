package model.graphic

import javafx.scene.canvas.GraphicsContext

interface Form {
    fun draw(gc: GraphicsContext)
}