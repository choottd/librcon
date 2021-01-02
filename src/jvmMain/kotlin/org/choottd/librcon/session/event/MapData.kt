package org.choottd.librcon.session.event

import org.choottd.librcon.gamestate.GameDate
import org.choottd.librcon.gamestate.MapState

data class MapData(
    val name: String,
    val landscape: MapState.Landscape,
    val dateStart: GameDate,
    val seed: Long,
    val height: Int,
    val width: Int
) {

    companion object {
        fun from(state: MapState) = MapData(
            state.name, state.landscape, state.dateStart,
            state.seed, state.height, state.width
        )
    }
}
