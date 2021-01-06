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

data class GlobalState(
    var gameState: GameState? = null,
    var dateCurrent: GameDate? = null,
    var protocol: ProtocolState? = null,
    val clients: MutableMap<Long, ClientState> = mutableMapOf(),
    val companies: MutableMap<Int, CompanyState> = mutableMapOf(),
    val commands: MutableMap<Int, CommandState> = mutableMapOf(),
    val pauseState: MutableMap<GameState.PauseMode, Boolean> = mutableMapOf()
) {

    fun setPauseMode(pauseMode: GameState.PauseMode, paused: Boolean) {
        if (!paused) {
            pauseState.remove(pauseMode)
        } else {
            pauseState[pauseMode] = true
        }
    }

    fun isPaused(): Boolean {
        return pauseState.containsValue(true)
    }

    fun getPauseMode() =
        pauseState.entries.firstOrNull { entry -> entry.value }?.key ?: GameState.PauseMode.UNPAUSED

}
