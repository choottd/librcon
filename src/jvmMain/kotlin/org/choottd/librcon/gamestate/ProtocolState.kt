package org.choottd.librcon.gamestate

import org.choottd.librcon.packet.data.ServerProtocol

data class ProtocolState(
    val version: Int,
    val supportedFrequencies: Map<ServerProtocol.AdminUpdateType, Set<ServerProtocol.AdminUpdateFrequency>>
) {

    fun isSupported(type: ServerProtocol.AdminUpdateType, freq: ServerProtocol.AdminUpdateFrequency) =
        supportedFrequencies[type]?.contains(freq) ?: false
}
