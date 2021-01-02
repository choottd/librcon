package org.choottd.librcon.session.event

import org.choottd.librcon.gamestate.GameDate
import org.choottd.librcon.gamestate.PastEconomyState
import java.math.BigInteger

data class PastEconomyData(
    val date: GameDate,
    val value: BigInteger,
    val cargo: Int,
    val performance: Int
) {

    companion object {
        fun from(state: PastEconomyState) = PastEconomyData(state.date, state.value, state.cargo, state.performance)
    }
}
