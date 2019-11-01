package model.graphic

import javafx.scene.canvas.GraphicsContext
import model.math.Point

interface Form {
    fun draw(gc: GraphicsContext)
    fun normalize(min: Point, max: Point): Form
    fun convertFromNormalized(min: Point, max: Point)
}