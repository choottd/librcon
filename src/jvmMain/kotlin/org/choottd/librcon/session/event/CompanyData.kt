package org.choottd.librcon.session.event

import org.choottd.librcon.gamestate.Color
import org.choottd.librcon.gamestate.CompanyState
import org.choottd.librcon.gamestate.GameDate
import java.math.BigInteger

data class CompanyData(
    val companyId: Int,
    val name: String,
    val president: String,
    val color: Color,
    val inaugurated: GameDate,
    val ai: Boolean,
    val bankruptcy: Int,
    val passwordProtected: Boolean,
    val value: BigInteger?,
    val economy: EconomyData?,
    val economyHistory: List<PastEconomyData>,
    val shares: List<Int>,
    val vehicles: Map<CompanyState.VehicleType, Int>,
    val stations: Map<CompanyState.VehicleType, Int>
) {

    companion object {
        fun from(state: CompanyState) = CompanyData(
            state.companyId, state.name, state.president,
            state.color, state.inaugurated, state.ai, state.bankruptcy, state.passwordProtected,
            state.value, state.economy?.let { EconomyData.from(it) },
            state.economyHistory.map { PastEconomyData.from(it) }, state.shares.toList(), state.vehicles,
            state.stations
        )
    }
}
