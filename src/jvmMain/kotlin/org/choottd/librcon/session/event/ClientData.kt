package org.choottd.librcon.session.event

import org.choottd.librcon.gamestate.ClientState
import org.choottd.librcon.gamestate.GameDate

data class ClientData(
    val clientId: Long,
    val name: String,
    val companyId: Int,
    val language: ClientState.NetworkLanguage,
    val address: String,
    val joinDate: GameDate
) {

    companion object {
        fun from(state: ClientState) = ClientData(
            state.clientId, state.name, state.companyId,
            state.language, state.address, state.joinDate
        )
    }
}
