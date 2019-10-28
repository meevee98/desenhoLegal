package model.enums

enum class BasicForm {
    POINT, LINE, CIRCLE, RECTANGLE, POLYGON;

    override fun toString(): String {
        return when (this) {
            POINT -> "Ponto"
            LINE -> "Linha"
            CIRCLE -> "Círculo"
            RECTANGLE -> "Retângulo"
            POLYGON -> "Polígono"
        }
    }
}