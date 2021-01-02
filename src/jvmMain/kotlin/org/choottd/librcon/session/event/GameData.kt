package org.choottd.librcon.session.event

import org.choottd.librcon.gamestate.GameState

data class GameData(
    val name: String,
    val gameVersion: String,
    val dedicated: Boolean,
    val map: MapData
) {

    companion object {
        fun from(state: GameState) = GameData(state.name, state.gameVersion, state.dedicated, MapData.from(state.map))
    }
}
