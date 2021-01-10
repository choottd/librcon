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

package org.choottd.librcon.session.event.data

import org.choottd.librcon.gamestate.GameDate
import org.choottd.librcon.gamestate.GameState
import org.choottd.librcon.gamestate.GlobalState

data class GlobalStateData(
    val gameData: GameData?,
    val gameDate: GameDate?,
    val protocol: ProtocolData?,
    val clients: Set<ClientData>,
    val companies: Set<CompanyData>,
    val commands: Set<CommandData>,
    val pauseMode: GameState.PauseMode
) {
    companion object {
        fun from(state: GlobalState) = GlobalStateData(
            state.gameState?.let { GameData.from(it) },
            state.dateCurrent,
            state.protocol?.let { ProtocolData.from(it) },
            state.clients.values.map { ClientData.from(it) }.toSet(),
            state.companies.values.map { CompanyData.from(it) }.toSet(),
            state.commands.values.map { CommandData.from(it) }.toSet(),
            state.getPauseMode()
        )
    }
}
