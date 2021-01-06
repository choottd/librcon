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

sealed class GameStatePacketData(type: PacketType) : PacketData(type)

class ServerWelcome(packet: InputPacket) : GameStatePacketData(packet.type) {
    val gameName: String = packet.readString()
    val gameVersion: String = packet.readString()
    val gameDedicated: Boolean = packet.readBool()
    val mapName: String = packet.readString()
    val mapSeed: Long = packet.readUint32()
    val mapLandscape: Int = packet.readUint8()
    val mapDateStart: Long = packet.readUint32()
    val mapWidth: Int = packet.readUint16()
    val mapHeight: Int = packet.readUint16()
}

class ServerDate(packet: InputPacket) : GameStatePacketData(packet.type) {
    val gameDate: Long = packet.readUint32()
}
