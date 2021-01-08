/*
 * Copyright 2021 Giordano Battilana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.choottd.librcon.session.event.data

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
