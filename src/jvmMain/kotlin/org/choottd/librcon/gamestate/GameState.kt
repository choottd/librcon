/*
 * Copyright (c) 2020. Giordano Battilana
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

data class GameState(
    val name: String,
    val gameVersion: String,
    val dedicated: Boolean,
    val map: MapState
) {

    enum class PauseMode(val value: Int) {
        UNPAUSED(0),  ///< A normal unpaused game
        PAUSED_NORMAL(1),  ///< A game normally paused
        PAUSED_SAVELOAD(2),  ///< A game paused for saving/loading
        PAUSED_JOIN(4),  ///< A game paused for 'pause_on_join'
        PAUSED_ERROR(8),  ///< A game paused because a (critical) error
        PAUSED_ACTIVE_CLIENTS(16),  ///< A game paused for 'min_active_clients'
        PAUSED_GAME_SCRIPT(32),  ///< A game paused by a game script

        /** Pause mode bits when paused for network reasons.  */
        PMB_PAUSED_NETWORK(PAUSED_ACTIVE_CLIENTS.value or PAUSED_JOIN.value);

        companion object {
            private val valuesMap = values().associateBy(PauseMode::value)
            fun valueOf(value: Int) = valuesMap[value]
        }
    }
}
