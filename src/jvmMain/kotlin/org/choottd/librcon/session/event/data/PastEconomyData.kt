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
