package org.choottd.librcon.session.event

import org.choottd.librcon.gamestate.CommandState

data class CommandData(
    val name: String,
    val value: Int,
) {

    companion object {
        fun from(state: CommandState) = CommandData(state.name, state.value)
    }
}
