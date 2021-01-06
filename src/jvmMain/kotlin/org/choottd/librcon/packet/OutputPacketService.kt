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

import org.choottd.librcon.packet.data.ServerProtocol
import org.choottd.librcon.session.event.ChatEvent

object OutputPacketService {

    fun adminJoin(password: String, botName: String, botVersion: String) =
        OutputPacket.Builder(PacketType.ADMIN_JOIN)
            .writeString(password)
            .writeString(botName)
            .writeString(botVersion)
            .build()

    fun adminChat(action: ChatEvent.NetworkAction, type: ChatEvent.DestType, dest: Long, message: String, data: Long) =
        OutputPacket.Builder(PacketType.ADMIN_CHAT)
            .writeUint8(action.ordinal)
            .writeUint8(type.ordinal)
            .writeUint32(dest)
            .writeString(message.trim().take(900))
            .writeUint64(data)
            .build()

    fun adminGamescript(json: String) =
        OutputPacket.Builder(PacketType.ADMIN_GAMESCRIPT)
            .writeString(json)
            .build()

    fun adminPing(value: Long) =
        OutputPacket.Builder(PacketType.ADMIN_PING)
            .writeUint32(value)
            .build()

    fun adminPoll(type: ServerProtocol.AdminUpdateType, data: Long) =
        OutputPacket.Builder(PacketType.ADMIN_POLL)
            .writeUint8(type.value)
            .writeUint32(data)
            .build()

    fun adminQuit() = OutputPacket.Builder(PacketType.ADMIN_QUIT).build()

    fun adminRcon(command: String) = OutputPacket.Builder(PacketType.ADMIN_RCON)
        .writeString(command)
        .build()

    fun adminUpdateFrequency(type: ServerProtocol.AdminUpdateType, frequency: ServerProtocol.AdminUpdateFrequency) =
        OutputPacket.Builder(PacketType.ADMIN_UPDATE_FREQUENCY)
            .writeUint16(type.value)
            .writeUint16(frequency.value)
            .build()

}
