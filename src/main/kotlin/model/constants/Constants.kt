package model.constants

import javafx.scene.paint.Color
import model.enums.BasicForm

object Constants {
    // region String
    const val WINDOW_TITLE = "Bora Desenhar"
    const val INVALID_INPUT = "Entrada inválida"
    // endregion

    // region Dimension
    const val HAIRLINE = 1
    const val DEFAULT_DRAW_DIAMETER = 4
    const val DEFAULT_PANE_PADDING = 10.0
    // endregion

    // region Enum
    val DEFAULT_GRAPHIC_FORM = BasicForm.POINT
    // endregion

    // region Color
    val DEFAULT_PRIMARY_COLOR: Color = Color.BLACK
    val DEFAULT_SECONDARY_COLOR: Color = Color.WHITE
    // endregion

}