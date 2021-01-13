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

@file:Suppress("unused")

package org.choottd.librcon.session

import kotlinx.coroutines.launch
import org.choottd.librcon.packet.OutputPacketService
import org.choottd.librcon.session.event.ChatEvent

fun Session.sendBroadcastAdminMessage(message: String) = launch {
    val packet = OutputPacketService.adminChat(
        ChatEvent.NetworkAction.SERVER_MESSAGE,
        ChatEvent.DestType.BROADCAST,
        0,
        message,
        0
    )
    queueOutputPacket(packet)
}

fun Session.sendPrivateAdminMessageToClient(clientId: Long, message: String) = launch {
    val packet = OutputPacketService.adminChat(
        ChatEvent.NetworkAction.SERVER_MESSAGE,
        ChatEvent.DestType.CLIENT,
        clientId,
        message,
        0
    )
    queueOutputPacket(packet)
}

fun Session.sendPublicChat(message: String) = launch {
    val packet =
        OutputPacketService.adminChat(ChatEvent.NetworkAction.CHAT, ChatEvent.DestType.BROADCAST, 0, message, 0)
    queueOutputPacket(packet)
}

fun Session.sendPrivateChat(clientId: Long, message: String) = launch {
    val packet = OutputPacketService.adminChat(
        ChatEvent.NetworkAction.CHAT_CLIENT,
        ChatEvent.DestType.CLIENT,
        clientId,
        message,
        0
    )
    queueOutputPacket(packet)
}

fun Session.sendTeamChat(companyId: Int, message: String) = launch {
    val packet = OutputPacketService.adminChat(
        ChatEvent.NetworkAction.SERVER_MESSAGE,
        ChatEvent.DestType.TEAM,
        companyId.toLong(),
        message,
        0
    )
    queueOutputPacket(packet)
}
