/*
 * Copyright 2021 Giordano Battilana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
