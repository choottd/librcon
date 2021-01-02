package org.choottd.librcon.gamestate

import java.math.BigInteger

class PastEconomyState(
    val date: GameDate,
    val value: BigInteger,
    val cargo: Int,
    val performance: Int
)
