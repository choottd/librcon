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

/**
 * Coroutine that manages the state about the clients
 */

suspend fun Session.clientsHandler(data: ClientsPacketData) {
    val event = when (data) {
        is ServerChat -> {
            val client = globalState.clients[data.clientId]
            if (client != null) ChatEvent(
                ChatEvent.NetworkAction.valueOf(data.networkAction),
                ChatEvent.DestType.valueOf(data.destinationType),
                client,
                data.message,
                data.data
            ) else null
        }

        is ServerClientJoin -> {
            val client = globalState.clients[data.clientId]
            if (client != null) ClientJoinEvent(ClientData.from(client)) else null
        }

        is ServerClientQuit -> {
            val client = globalState.clients[data.clientId]
            if (client != null) ClientQuitEvent(ClientData.from(client)) else null
        }

        is ServerClientUpdate -> {
            val client = globalState.clients[data.clientId]
            if (client != null) {
                client.name = data.name
                client.companyId = data.companyId
                ClientUpdateEvent(ClientData.from(client))
            } else null
        }

        is ServerClientError -> {
            val client = globalState.clients.remove(data.clientId)
            val error = ServerErrorEvent.NetworkErrorCode.valueOf(data.error)
            if (client != null) ClientErrorEvent(ClientData.from(client), error) else null
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
