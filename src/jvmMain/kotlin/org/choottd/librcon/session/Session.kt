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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.choottd.librcon.connection.ServerConnection
import org.choottd.librcon.gamestate.GlobalState
import org.choottd.librcon.packet.InputPacketService
import org.choottd.librcon.packet.InvalidPacketException
import org.choottd.librcon.packet.OutputPacketService
import org.choottd.librcon.packet.PacketType.*
import org.choottd.librcon.packet.data.*
import org.choottd.librcon.session.event.ChatEvent
import org.choottd.librcon.session.event.SessionEvent
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
class Session(
    private val botName: String,
    private val botVersion: String,
    private val password: String,
    host: String,
    port: Int
) : CoroutineScope {

    private var state = State.NONE
    private val job = Job()
    private val connection: ServerConnection = ServerConnection(host, port)

    internal val globalState = GlobalState()

    private val _sessionEvents = MutableSharedFlow<SessionEvent>()
    val sessionEvents: SharedFlow<SessionEvent>
        get() = _sessionEvents.asSharedFlow()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun open(): Job = launch {
        if (state == State.STOPPED) {
            throw RuntimeException("Session is closed")
        }

        // init the connection
        connection.open()
        state = State.STARTED

        // start to listen for packets
        packetsHandler()

        // send the join packet to authenticate
        sendAdminJoin()
    }

    fun close() = launch {
        sendAdminQuit()
        state = State.STOPPED
        job.cancel()
    }

    private fun packetsHandler() = launch {
        while (true) {
            val inputPacket = connection.readPacket()
            val packetData = InputPacketService.parseData(inputPacket)
            logger.debug(packetData.toString())

            when (packetData.type) {
                ADMIN_JOIN,
                ADMIN_QUIT,
                ADMIN_POLL,
                ADMIN_CHAT,
                ADMIN_RCON,
                ADMIN_GAMESCRIPT,
                ADMIN_UPDATE_FREQUENCY,
                ADMIN_PING -> {
                    logger.error("Unexpected packet of type ${packetData.type} received!")
                    throw InvalidPacketException("Unexpected packet of type ${packetData.type}")
                }

                INVALID_ADMIN_PACKET -> {
                    logger.error("Received INVALID_ADMIN_PACKET!")
                    throw InvalidPacketException("Received INVALID_ADMIN_PACKET!")
                }

                SERVER_DATE,
                SERVER_WELCOME -> gameStateHandler(packetData as GameStatePacketData)

                SERVER_COMPANY_NEW,
                SERVER_COMPANY_STATS,
                SERVER_COMPANY_UPDATE,
                SERVER_COMPANY_REMOVE,
                SERVER_COMPANY_ECONOMY,
                SERVER_COMPANY_INFO -> companiesHandler(packetData as CompaniesPacketData)

                SERVER_CLIENT_JOIN,
                SERVER_CLIENT_QUIT,
                SERVER_CLIENT_UPDATE,
                SERVER_CLIENT_INFO,
                SERVER_CLIENT_ERROR,
                SERVER_CHAT -> clientsHandler(packetData as ClientsPacketData)

                SERVER_NEWGAME,
                SERVER_BANNED,
                SERVER_SHUTDOWN,
                SERVER_PROTOCOL,
                SERVER_CONSOLE,
                SERVER_FULL,
                SERVER_RCON,
                SERVER_RCON_END,
                SERVER_GAMESCRIPT,
                SERVER_CMD_NAMES,
                SERVER_CMD_LOGGING,
                SERVER_PONG,
                SERVER_ERROR -> operationsHandler(packetData as OperationsPacketData)
            }
        }
    }

    internal suspend fun sendEvent(event: SessionEvent?) {
        if (event != null) {
            logger.debug("Emitting event $event")
            _sessionEvents.emit(event)
        }
    }

    fun fetchClient(clientId: Long) = sendAdminPoll(ServerProtocol.AdminUpdateType.CLIENT_INFO, clientId)

    fun fetchClients() = sendAdminPoll(ServerProtocol.AdminUpdateType.CLIENT_INFO, Long.MAX_VALUE)

    fun fetchCompany(companyId: Int) = sendAdminPoll(ServerProtocol.AdminUpdateType.COMPANY_INFO, companyId.toLong())

    fun fetchCompanies() = sendAdminPoll(ServerProtocol.AdminUpdateType.COMPANY_INFO, Long.MAX_VALUE)

    fun fetchCompanyEconomy() = sendAdminPoll(ServerProtocol.AdminUpdateType.COMPANY_ECONOMY)

    fun fetchCompanyStats() = sendAdminPoll(ServerProtocol.AdminUpdateType.COMPANY_STATS)

    fun fetchCommands() = sendAdminPoll(ServerProtocol.AdminUpdateType.CMD_NAMES)

    fun fetchCurrentDate() = sendAdminPoll(ServerProtocol.AdminUpdateType.DATE)

    private fun sendAdminJoin() = launch {
        val adminJoinPacket = OutputPacketService.adminJoin(password, botName, botVersion)
        connection.writePacket(adminJoinPacket)
    }

    private fun sendAdminQuit() = launch {
        val adminQuitPacket = OutputPacketService.adminQuit()
        connection.writePacket(adminQuitPacket)
    }

    fun sendBroadcastAdminMessage(message: String) = launch {
        val packet = OutputPacketService.adminChat(
            ChatEvent.NetworkAction.SERVER_MESSAGE,
            ChatEvent.DestType.BROADCAST,
            0,
            message,
            0
        )
        connection.writePacket(packet)
    }

    fun sendPrivateAdminMessageToClient(clientId: Long, message: String) = launch {
        val packet = OutputPacketService.adminChat(
            ChatEvent.NetworkAction.SERVER_MESSAGE,
            ChatEvent.DestType.CLIENT,
            clientId,
            message,
            0
        )
        connection.writePacket(packet)
    }

    fun sendPublicChat(message: String) = launch {
        val packet =
            OutputPacketService.adminChat(ChatEvent.NetworkAction.CHAT, ChatEvent.DestType.BROADCAST, 0, message, 0)
        connection.writePacket(packet)
    }

    fun sendPrivateChat(clientId: Long, message: String) = launch {
        val packet = OutputPacketService.adminChat(
            ChatEvent.NetworkAction.CHAT_CLIENT,
            ChatEvent.DestType.CLIENT,
            clientId,
            message,
            0
        )
        connection.writePacket(packet)
    }

    fun sendTeamChat(companyId: Int, message: String) = launch {
        val packet = OutputPacketService.adminChat(
            ChatEvent.NetworkAction.SERVER_MESSAGE,
            ChatEvent.DestType.TEAM,
            companyId.toLong(),
            message,
            0
        )
        connection.writePacket(packet)
    }

    fun sendAdminGameScript(json: String) = launch {
        val packet = OutputPacketService.adminGamescript(json)
        connection.writePacket(packet)
    }

    fun sendAdminUpdateFrequency(type: ServerProtocol.AdminUpdateType, frequency: ServerProtocol.AdminUpdateFrequency) =
        launch {
            val packet = OutputPacketService.adminUpdateFrequency(type, frequency)
            connection.writePacket(packet)
        }

    fun sendAdminPing(value: Long) = launch {
        val packet = OutputPacketService.adminPing(value)
        connection.writePacket(packet)
    }

    private fun sendAdminPoll(type: ServerProtocol.AdminUpdateType, data: Long = 0) = launch {
        if (globalState.protocol?.isSupported(type, ServerProtocol.AdminUpdateFrequency.POLL) != true) {
            logger.error("The server does not support POLL for $type")
            throw InvalidPollingException("The server does not support POLL for $type")
        }
        val packet = OutputPacketService.adminPoll(type, data)
        connection.writePacket(packet)
    }

    fun sendAdminRcon(command: String) = launch {
        val packet = OutputPacketService.adminRcon(command)
        connection.writePacket(packet)
    }

    private enum class State {
        NONE, STARTED, STOPPED
    }

    companion object {
        internal val logger = LoggerFactory.getLogger(Session::class.java)
    }
}
