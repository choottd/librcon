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
