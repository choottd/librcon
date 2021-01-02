package org.choottd.librcon.gamestate

enum class Color(val value: Int) {
    DARK_BLUE(0),
    PALE_GREEN(1),
    PINK(2),
    YELLOW(3),
    RED(4),
    LIGHT_BLUE(5),
    GREEN(6),
    DARK_GREEN(7),
    BLUE(8),
    CREAM(9),
    MAUVE(10),
    PURPLE(11),
    ORANGE(12),
    BROWN(13),
    GREY(14),
    WHITE(15),
    END(16),
    INVALID(0xFF);

    companion object {
        private val valuesMap = values().associateBy(Color::value)
        fun valueOf(value: Int) = valuesMap[value] ?: INVALID
    }
}
