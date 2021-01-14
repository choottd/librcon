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

package org.choottd.librcon.packet

import org.choottd.librcon.packet.InputPacketType.*
import org.choottd.librcon.packet.data.*

object InputPacketService {

    fun parseData(packet: InputPacket): PacketData = when (packet.type) {
        INVALID_ADMIN_PACKET -> InvalidPacket(packet)
        SERVER_FULL -> ServerFull(packet)
        SERVER_ERROR -> ServerError(packet)
        SERVER_PROTOCOL -> ServerProtocol(packet)
        SERVER_WELCOME -> ServerWelcome(packet)
        SERVER_BANNED -> ServerBanned(packet)
        SERVER_NEWGAME -> ServerNewGame(packet)
        SERVER_SHUTDOWN -> ServerShutdown(packet)
        SERVER_DATE -> ServerDate(packet)
        SERVER_CLIENT_JOIN -> ServerClientJoin(packet)
        SERVER_CLIENT_INFO -> ServerClientInfo(packet)
        SERVER_CLIENT_UPDATE -> ServerClientUpdate(packet)
        SERVER_CLIENT_QUIT -> ServerClientQuit(packet)
        SERVER_CLIENT_ERROR -> ServerClientError(packet)
        SERVER_COMPANY_NEW -> ServerCompanyNew(packet)
        SERVER_COMPANY_INFO -> ServerCompanyInfo(packet)
        SERVER_COMPANY_UPDATE -> ServerCompanyUpdate(packet)
        SERVER_COMPANY_REMOVE -> ServerCompanyRemove(packet)
        SERVER_COMPANY_ECONOMY -> ServerCompanyEconomy(packet)
        SERVER_COMPANY_STATS -> ServerCompanyStats(packet)
        SERVER_CHAT -> ServerChat(packet)
        SERVER_RCON -> ServerRCon(packet)
        SERVER_CONSOLE -> ServerConsole(packet)
        SERVER_CMD_NAMES -> ServerCmdNames(packet)
        SERVER_CMD_LOGGING -> ServerCmdLogging(packet)
        SERVER_GAMESCRIPT -> ServerGameScript(packet)
        SERVER_RCON_END -> ServerRConEnd(packet)
        SERVER_PONG -> ServerPong(packet)
    }

}
