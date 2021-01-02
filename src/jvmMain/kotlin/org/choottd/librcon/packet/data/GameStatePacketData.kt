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
