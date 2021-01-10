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

package org.choottd.librcon.session

import org.choottd.librcon.gamestate.ClientState
import org.choottd.librcon.gamestate.GameStateService
import org.choottd.librcon.packet.data.*
import org.choottd.librcon.session.event.*
import org.choottd.librcon.session.event.data.ClientData

/**
 * Coroutine that manages the state about the clients
 */
internal suspend fun Session.clientsHandler(data: ClientsPacketData) {
    val event = when (data) {
        is ServerChat -> globalState.clients[data.clientId]
            ?.let {
                ChatEvent(
                    ChatEvent.NetworkAction.valueOf(data.networkAction),
                    ChatEvent.DestType.valueOf(data.destinationType),
                    it,
                    data.message,
                    data.data
                )
            }
            ?: run {
                fetchClient(data.clientId)
                null
            }

        is ServerClientJoin -> globalState.clients[data.clientId]
            ?.let { ClientJoinEvent(ClientData.from(it)) }
            ?: run {
                fetchClient(data.clientId)
                null
            }

        is ServerClientQuit -> globalState.clients[data.clientId]
            ?.let {
                ClientQuitEvent(ClientData.from(it))
            }
            ?: run {
                fetchClient(data.clientId)
                null
            }

        is ServerClientUpdate -> globalState.clients[data.clientId]
            ?.let {
                it.name = data.name
                it.companyId = data.companyId
                ClientUpdateEvent(ClientData.from(it))
            }
            ?: run {
                fetchClient(data.clientId)
                null
            }

        is ServerClientError -> globalState.clients.remove(data.clientId)
            ?.let {
                val error = ServerErrorEvent.NetworkErrorCode.valueOf(data.error)
                ClientErrorEvent(ClientData.from(it), error)
            }
            ?: run {
                fetchClient(data.clientId)
                null
            }

        is ServerClientInfo -> {
            val client = ClientState(
                data.clientId, data.name, data.companyId, ClientState.NetworkLanguage.valueOf(data.language),
                data.address, GameStateService.convertDate(data.joinDate)
            )
            globalState.clients[client.clientId] = client
            ClientUpdateEvent(ClientData.from(client))
        }
    }

    sendEvent(event)
}
