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
import org.choottd.librcon.packet.data.ServerProtocol

fun Session.fetchAllData() = launch {
    fetchCommands()
    fetchGameDate()
    fetchClients()
    fetchCompanies().join() // we want to have the companies before updating the rest of their data
    fetchCompaniesStats()
    fetchCompaniesEconomies()
}

fun Session.fetchClient(clientId: Long) = sendAdminPoll(ServerProtocol.AdminUpdateType.CLIENT_INFO, clientId)

fun Session.fetchClients() = sendAdminPoll(ServerProtocol.AdminUpdateType.CLIENT_INFO, Long.MAX_VALUE)

fun Session.fetchCompany(companyId: Int) =
    sendAdminPoll(ServerProtocol.AdminUpdateType.COMPANY_INFO, companyId.toLong())

fun Session.fetchCompanies() = sendAdminPoll(ServerProtocol.AdminUpdateType.COMPANY_INFO, Long.MAX_VALUE)

fun Session.fetchCompaniesEconomies() = sendAdminPoll(ServerProtocol.AdminUpdateType.COMPANY_ECONOMY)

fun Session.fetchCompaniesStats() = sendAdminPoll(ServerProtocol.AdminUpdateType.COMPANY_STATS)

fun Session.fetchCommands() = sendAdminPoll(ServerProtocol.AdminUpdateType.CMD_NAMES)

fun Session.fetchGameDate() = sendAdminPoll(ServerProtocol.AdminUpdateType.DATE)

fun Session.sendAdminRcon(command: String) = launch {
    val packet = OutputPacketService.adminRcon(command)
    connection.writePacket(packet)
}

fun Session.sendAdminGameScript(json: String) = launch {
    val packet = OutputPacketService.adminGamescript(json)
    connection.writePacket(packet)
}

fun Session.sendAdminUpdateFrequency(
    type: ServerProtocol.AdminUpdateType,
    frequency: ServerProtocol.AdminUpdateFrequency
) =
    launch {
        val packet = OutputPacketService.adminUpdateFrequency(type, frequency)
        connection.writePacket(packet)
    }

fun Session.sendAdminPing(value: Long) = launch {
    val packet = OutputPacketService.adminPing(value)
    connection.writePacket(packet)
}

private fun Session.sendAdminPoll(type: ServerProtocol.AdminUpdateType, data: Long = 0) = launch {
    if (globalState.protocol?.isSupported(type, ServerProtocol.AdminUpdateFrequency.POLL) != true) {
        Session.logger.error("The server does not support POLL for $type")
        throw InvalidPollingException("The server does not support POLL for $type")
    }
    val packet = OutputPacketService.adminPoll(type, data)
    connection.writePacket(packet)
}
