package model.constants

import javafx.scene.paint.Color
import model.enums.BasicForm

object Constants {
    // region String
    const val WINDOW_TITLE = "Bora Desenhar"
    const val INVALID_INPUT = "Entrada inválida"
    const val OPEN_FILE_ERROR = "Erro ao abrir"
    const val SAVE_FILE_ERROR = "Erro ao salvar"
    // endregion

    // region Dimension
    const val HAIRLINE = 1
    const val DEFAULT_BORDER_WIDTH = 2.0
    const val DEFAULT_DRAW_DIAMETER = 4
    const val DEFAULT_PANE_PADDING = 10.0
    const val CANVAS_WIDTH = 3000.0
    const val CANVAS_HEIGHT = 2000.0
    // endregion

    // region Enum
    val DEFAULT_GRAPHIC_FORM = BasicForm.POINT
    // endregion

    // region Color
    val DEFAULT_PRIMARY_COLOR: Color = Color.BLACK
    val DEFAULT_SECONDARY_COLOR: Color = Color.WHITE
    val DEFAULT_SELECTION_COLOR: Color = Color.DODGERBLUE
    val DEFAULT_BACKGROUND_COLOR: Color = Color.WHITE
    val DEFAULT_SEPARATOR_COLOR: Color = Color.GAINSBORO
    // endregion

}