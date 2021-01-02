package org.choottd.librcon.session.event

import org.choottd.librcon.gamestate.ProtocolState
import org.choottd.librcon.packet.data.ServerProtocol

data class ProtocolData(
    val version: Int,
    val supportedFrequencies: Map<ServerProtocol.AdminUpdateType, Set<ServerProtocol.AdminUpdateFrequency>>
) {

    companion object {
        fun from(state: ProtocolState) = ProtocolData(state.version, state.supportedFrequencies)
    }
}
