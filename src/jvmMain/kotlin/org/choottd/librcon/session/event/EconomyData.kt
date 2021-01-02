package org.choottd.librcon.session.event

import org.choottd.librcon.gamestate.EconomyState
import org.choottd.librcon.gamestate.GameDate
import java.math.BigInteger

data class EconomyData(
    val date: GameDate,
    val money: BigInteger,
    val loan: BigInteger,
    val income: BigInteger
) {

    companion object {
        fun from(state: EconomyState) = EconomyData(state.date, state.money, state.loan,state.income)
    }
}
