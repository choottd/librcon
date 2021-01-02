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
