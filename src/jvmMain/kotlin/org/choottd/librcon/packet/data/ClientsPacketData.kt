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

package org.choottd.librcon.packet.data

import org.choottd.librcon.packet.InputPacket
import org.choottd.librcon.packet.PacketType
import java.math.BigInteger

sealed class ClientsPacketData(type: PacketType) : PacketData(type)

class ServerClientJoin(packet: InputPacket) : ClientsPacketData(packet.type) {
    val clientId: Long = packet.readUint32()
}

class ServerClientQuit(packet: InputPacket) : ClientsPacketData(packet.type) {
    val clientId: Long = packet.readUint32()
}

class ServerClientUpdate(packet: InputPacket) : ClientsPacketData(packet.type) {
    val clientId: Long = packet.readUint32()
    val name: String = packet.readString()
    val companyId: Int = packet.readUint8()
}

class ServerClientError(packet: InputPacket) : ClientsPacketData(packet.type) {
    val clientId: Long = packet.readUint32()
    val error: Int = packet.readUint8()
}

class ServerClientInfo(packet: InputPacket) : ClientsPacketData(packet.type) {
    val clientId: Long = packet.readUint32()
    val address: String = packet.readString()
    val name: String = packet.readString()
    val language: Int = packet.readUint8()
    val joinDate: Long = packet.readUint32()
    val companyId: Int = packet.readUint8()
}

class ServerChat(packet: InputPacket) : ClientsPacketData(packet.type) {
    val networkAction: Int = packet.readUint8()
    val destinationType: Int = packet.readUint8()
    val clientId: Long = packet.readUint32()
    val message: String = packet.readString()
    val data: BigInteger = packet.readUint64()
}
