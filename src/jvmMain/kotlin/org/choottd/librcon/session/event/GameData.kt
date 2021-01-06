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

package org.choottd.librcon.session.event

import org.choottd.librcon.gamestate.GameState

data class GameData(
    val name: String,
    val gameVersion: String,
    val dedicated: Boolean,
    val map: MapData
) {

    companion object {
        fun from(state: GameState) = GameData(state.name, state.gameVersion, state.dedicated, MapData.from(state.map))
    }
}
