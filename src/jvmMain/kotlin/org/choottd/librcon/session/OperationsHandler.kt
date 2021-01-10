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

import org.choottd.librcon.gamestate.Color
import org.choottd.librcon.gamestate.CommandState
import org.choottd.librcon.gamestate.GameState
import org.choottd.librcon.gamestate.ProtocolState
import org.choottd.librcon.packet.data.*
import org.choottd.librcon.session.event.*
import org.choottd.librcon.session.event.data.*

/**
 * Coroutine that manages generic operations
 */
internal suspend fun Session.operationsHandler(data: OperationsPacketData) {
    val event = when (data) {
        is ServerBanned -> {
            close()
            ServerBannedEvent()
        }

        is ServerNewGame -> NewGameEvent()

        is ServerShutdown -> {
            close()
            ServerShutdownEvent()
        }

        is ServerFull -> {
            close()
            ServerFullEvent()
        }

        is ServerCmdLogging -> {
            val client = globalState.clients[data.clientId]
            val company = globalState.companies[data.companyId]
            val command = globalState.commands[data.commandId]

            if (client == null) {
                fetchClient(data.clientId)
            }

            if (company == null) {
                fetchCompany(data.companyId)
            }

            if (command == null) {
                fetchCommands()
            }

            if (client == null || company == null || command == null) {
                null
            } else {
                if ("CmdPause" == command.name) {
                    val pauseMode = GameState.PauseMode.valueOf(data.p1.toInt())
                    val paused = data.p2 != 0L
                    if (pauseMode != null) {
                        globalState.setPauseMode(pauseMode, paused)
                    } else {
                        Session.logger.error("pauseMode is null! p1: ${data.p1}, p2: ${data.p2}")
                    }
                }

                ServerCmdEvent(
                    ServerCmdData(
                        ClientData.from(client), CompanyData.from(company), CommandData.from(command),
                        data.p1, data.p2, data.title, data.text, data.frame
                    )
                )
            }
        }

        is ServerCmdNames -> {
            globalState.commands.clear()
            data.commands.forEach { (name, value) -> globalState.commands[value] = CommandState(name, value) }
            null
        }

        is ServerError -> {
            close()
            ServerErrorEvent(ServerErrorEvent.NetworkErrorCode.valueOf(data.error))
        }

        is ServerProtocol -> {
            val protocol = ProtocolState(data.version, data.supportedFrequencies)
            globalState.protocol = protocol
            ServerProtocolEvent(ProtocolData.from(protocol))
        }

        is ServerConsole -> ServerConsoleEvent(data.origin, data.message)

        is ServerGameScript -> ServerGameScriptEvent(data.gameScript)

        is ServerPong -> ServerPongEvent(data.payload)

        is ServerRCon -> ServerRConEvent(Color.valueOf(data.color), data.message)

        is ServerRConEnd -> ServerRConEndEvent()
    }

    sendEvent(event)
}
