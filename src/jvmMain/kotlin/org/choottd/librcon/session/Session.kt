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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.choottd.librcon.connection.ServerConnection
import org.choottd.librcon.gamestate.GlobalState
import org.choottd.librcon.packet.InputPacketService
import org.choottd.librcon.packet.InputPacketType.*
import org.choottd.librcon.packet.OutputPacket
import org.choottd.librcon.packet.OutputPacketService
import org.choottd.librcon.packet.data.ClientsPacketData
import org.choottd.librcon.packet.data.CompaniesPacketData
import org.choottd.librcon.packet.data.GameStatePacketData
import org.choottd.librcon.packet.data.OperationsPacketData
import org.choottd.librcon.session.event.SessionClosedEvent
import org.choottd.librcon.session.event.SessionEvent
import org.slf4j.LoggerFactory
import java.io.IOException
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
class Session(
    private val botName: String,
    private val botVersion: String,
    private val password: String,
    host: String,
    port: Int
) : CoroutineScope {

    internal var state = State.NONE
    private val job = Job()
    private val connection: ServerConnection = ServerConnection(host, port)
    private val outputChannel = Channel<OutputPacket>(Channel.UNLIMITED)

    internal val globalState = GlobalState()

    private val _sessionEvents = MutableSharedFlow<SessionEvent>()
    val sessionEvents: SharedFlow<SessionEvent>
        get() = _sessionEvents.asSharedFlow()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun open(): Job = launch {
        if (state == State.SESSION_STOPPED) {
            throw RuntimeException("Session is closed")
        }

        // init the connection
        connection.open()
        state = State.SESSION_OPENED

        // start to listen for packets
        inputPacketHandler()
        // start also to write packets
        outputPacketHandler()

        // send the join packet to authenticate
        sendAdminJoin()
    }

    fun close() = launch {
        sendAdminQuit()
        state = State.SESSION_STOPPED
        sendEvent(SessionClosedEvent())
        job.cancel()
    }

    private fun inputPacketHandler() = launch {
        while (true) {
            val inputPacket = try {
                connection.readPacket()
            } catch (ex: IOException) {
                logger.warn("Socket read failed", ex)
                continue
            }
            val packetData = InputPacketService.parseData(inputPacket)
            logger.debug("[RECEIVED] {}", packetData)

            when (packetData.type) {
                INVALID_ADMIN_PACKET -> logger.error("Received INVALID_ADMIN_PACKET!")

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

    private fun outputPacketHandler() = launch {
        while (true) {
            val packet = outputChannel.receive()
            connection.writePacket(packet)
        }
    }

    internal suspend fun queueOutputPacket(packet: OutputPacket) {
        when (state) {
            State.WELCOME_RECEIVED -> {
                outputChannel.send(packet)
                logger.debug("[SENT] {}", packet)
            }
            else -> {
                logger.warn("Session in invalid state $state")
            }
        }

    }

    internal suspend fun sendEvent(event: SessionEvent?) {
        if (event != null) {
            logger.debug("Emitting event {}", event)
            _sessionEvents.emit(event)
        }
    }

    private fun sendAdminJoin() = launch {
        val adminJoinPacket = OutputPacketService.adminJoin(password, botName, botVersion)
        outputChannel.send(adminJoinPacket)
    }

    private fun sendAdminQuit() = launch {
        val adminQuitPacket = OutputPacketService.adminQuit()
        outputChannel.send(adminQuitPacket)
    }

    internal enum class State {
        NONE, SESSION_OPENED, PROTOCOL_RECEIVED, WELCOME_RECEIVED, SESSION_STOPPED
    }

    companion object {
        internal val logger = LoggerFactory.getLogger(Session::class.java)
    }
}
