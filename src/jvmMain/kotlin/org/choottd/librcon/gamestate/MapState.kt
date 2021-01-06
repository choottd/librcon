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

data class MapState(
    val name: String,
    val landscape: Landscape,
    val dateStart: GameDate,
    val seed: Long,
    val height: Int,
    val width: Int
) {

    enum class Landscape(val value: Int) {
        UNKNOWN(-1),
        TEMPERATE(0),
        ARCTIC(1),
        TROPIC(2),
        TOYLAND(3),
        NUM_LANDSCAPE(4);

        companion object {
            private val valuesMap = values().associateBy(Landscape::value)
            fun valueOf(value: Int) = valuesMap[value] ?: UNKNOWN
        }
    }

}
