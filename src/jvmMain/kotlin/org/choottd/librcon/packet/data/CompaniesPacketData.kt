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

import org.choottd.librcon.gamestate.CompanyState
import org.choottd.librcon.packet.InputPacket
import org.choottd.librcon.packet.InputPacketType
import java.math.BigInteger

sealed class CompaniesPacketData(type: InputPacketType) : PacketData(type)

class ServerCompanyInfo(packet: InputPacket) : CompaniesPacketData(packet.type) {
    val companyId: Int = packet.readUint8()
    val name: String = packet.readString()
    val president: String = packet.readString()
    val color: Int = packet.readUint8()
    val passwordProtected: Boolean = packet.readBool()
    val inaugurated: Long = packet.readUint32()
    val ai: Boolean = packet.readBool()
}

class ServerCompanyEconomy(packet: InputPacket) : CompaniesPacketData(packet.type) {
    val companyId: Int = packet.readUint8()
    val economy: EconomyCurrent = EconomyCurrent(
        packet.readUint64(),
        packet.readUint64(),
        packet.readUint64()
    )
    val economyQM1: EconomyPrevQuarter = EconomyPrevQuarter(
        packet.readUint16(),
        packet.readUint64(),
        packet.readUint16(),
    )
    val economyQM2: EconomyPrevQuarter = EconomyPrevQuarter(
        packet.readUint16(),
        packet.readUint64(),
        packet.readUint16(),
    )
}

data class EconomyCurrent(
    val money: BigInteger,
    val loan: BigInteger,
    val income: BigInteger,
)

data class EconomyPrevQuarter(
    val cargo: Int,
    val value: BigInteger,
    val performance: Int
)

class ServerCompanyNew(packet: InputPacket) : CompaniesPacketData(packet.type) {
    val companyId: Int = packet.readUint8()
}

class ServerCompanyRemove(packet: InputPacket) : CompaniesPacketData(packet.type) {
    val companyId: Int = packet.readUint8()
    val removeReason: Int = packet.readUint8()
}

class ServerCompanyStats(packet: InputPacket) : CompaniesPacketData(packet.type) {
    val companyId: Int = packet.readUint8()
    val vehicles: Map<CompanyState.VehicleType, Int> = CompanyState.VehicleType.values()
        .map { it to packet.readUint16() }
        .toMap()
    val stations: Map<CompanyState.VehicleType, Int> = CompanyState.VehicleType.values()
        .map { it to packet.readUint16() }
        .toMap()
}

class ServerCompanyUpdate(packet: InputPacket) : CompaniesPacketData(packet.type) {
    val companyId: Int = packet.readUint8()
    val name: String = packet.readString()
    val president: String = packet.readString()
    val color: Int = packet.readUint8()
    val passwordProtected: Boolean = packet.readBool()
    val bankruptcy: Int = packet.readUint8()
    val shares: IntArray = intArrayOf(packet.readUint8(), packet.readUint8(), packet.readUint8(), packet.readUint8())
}
