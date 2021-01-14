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
import org.choottd.librcon.packet.InputPacketType

sealed class OperationsPacketData(type: InputPacketType) : PacketData(type)

class ServerNewGame(packet: InputPacket) : OperationsPacketData(packet.type)

class ServerBanned(packet: InputPacket) : OperationsPacketData(packet.type)

class ServerShutdown(packet: InputPacket) : OperationsPacketData(packet.type)

class ServerFull(packet: InputPacket) : OperationsPacketData(packet.type)

class InvalidPacket(packet: InputPacket) : OperationsPacketData(packet.type)

class ServerPong(packet: InputPacket) : OperationsPacketData(packet.type) {
    val payload: Long = packet.readUint32()
}

class ServerRCon(packet: InputPacket) : OperationsPacketData(packet.type) {
    val color: Int = packet.readUint16()
    val message: String = packet.readString()
}

class ServerRConEnd(packet: InputPacket) : OperationsPacketData(packet.type)

class ServerGameScript(packet: InputPacket) : OperationsPacketData(packet.type) {
    val gameScript: String = packet.readString()
}

class ServerCmdLogging(packet: InputPacket) : OperationsPacketData(packet.type) {
    val clientId: Long = packet.readUint32()
    val companyId: Int = packet.readUint8()
    val commandId: Int = packet.readUint16()
    val p1: Long = packet.readUint32()
    val p2: Long = packet.readUint32()
    val title: Long = packet.readUint32()
    val text: String = packet.readString()
    val frame: Long = packet.readUint32()
}

class ServerCmdNames(packet: InputPacket) : OperationsPacketData(packet.type) {
    val commands: List<Pair<String, Int>>

    init {
        val commands = mutableListOf<Pair<String, Int>>()
        while (packet.readBool()) {
            val value = packet.readUint16()
            val name = packet.readString()
            commands.add(name to value)
        }
        this.commands = commands.toList()
    }
}

class ServerError(packet: InputPacket) : OperationsPacketData(packet.type) {
    val error: Int = packet.readUint8()
}

class ServerConsole(packet: InputPacket) : OperationsPacketData(packet.type) {
    val origin: String = packet.readString()
    val message: String = packet.readString()
}

class ServerProtocol(packet: InputPacket) : OperationsPacketData(packet.type) {
    val version: Int = packet.readUint8()
    val supportedFrequencies: Map<AdminUpdateType, Set<AdminUpdateFrequency>>

    init {
        val supportedFrequencies = mutableMapOf<AdminUpdateType, MutableSet<AdminUpdateFrequency>>()

        while (packet.readBool()) {
            val adminUpdateType = AdminUpdateType.valueOf(packet.readUint16())
            var adminUpdateFrequencyValues = packet.readUint16()

            if (adminUpdateType != null)
                while (adminUpdateFrequencyValues > 0) {
                    val adminUpdateFrequencyValue = Integer.lowestOneBit(adminUpdateFrequencyValues)
                    val adminUpdateFrequency = AdminUpdateFrequency.valueOf(adminUpdateFrequencyValue)
                    if (adminUpdateFrequency != null) {
                        supportedFrequencies
                            .getOrPut(adminUpdateType, { mutableSetOf() })
                            .add(adminUpdateFrequency)
                        adminUpdateFrequencyValues -= adminUpdateFrequencyValue
                    }
                }

        }

        this.supportedFrequencies = supportedFrequencies.toMap().mapValues { (_, mSet) -> mSet.toSet() }
    }

    enum class AdminUpdateType(val value: Int) {
        DATE(0),
        CLIENT_INFO(1),
        COMPANY_INFO(2),
        COMPANY_ECONOMY(3),
        COMPANY_STATS(4),
        CHAT(5),
        CONSOLE(6),
        CMD_NAMES(7),
        CMD_LOGGING(8),
        GAMESCRIPT(9),
        END(10);

        companion object {
            private val valuesMap = values().associateBy(AdminUpdateType::value)
            fun valueOf(value: Int) = valuesMap[value]
        }
    }

    enum class AdminUpdateFrequency(val value: Int) {
        POLL(0x01),
        DAILY(0x02),
        WEEKLY(0x04),
        MONTHLY(0x08),
        QUARTERLY(0x10),
        ANUALLY(0x20),
        AUTOMATIC(0x40);

        companion object {
            private val valuesMap = values().associateBy(AdminUpdateFrequency::value)
            fun valueOf(value: Int) = valuesMap[value]
        }
    }
}
