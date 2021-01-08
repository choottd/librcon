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

import org.choottd.librcon.gamestate.ClientState
import org.choottd.librcon.gamestate.GameDate

data class ClientData(
    val clientId: Long,
    val name: String,
    val companyId: Int,
    val language: ClientState.NetworkLanguage,
    val address: String,
    val joinDate: GameDate
) {

    companion object {
        fun from(state: ClientState) = ClientData(
            state.clientId, state.name, state.companyId,
            state.language, state.address, state.joinDate
        )
    }
}
