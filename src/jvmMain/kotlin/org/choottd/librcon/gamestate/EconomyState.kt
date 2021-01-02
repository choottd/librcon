package org.choottd.librcon.gamestate

import java.math.BigInteger

class EconomyState(
    val date: GameDate,
    val money: BigInteger,
    val loan: BigInteger,
    val income: BigInteger
) {

    fun isSameQuarter(economy: EconomyState): Boolean {
        return date.year == economy.date.year && date.quarter == economy.date.quarter
    }
}
