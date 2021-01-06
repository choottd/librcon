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

package org.choottd.librcon.gamestate

import java.math.BigInteger

class CompanyState(
    val companyId: Int,
    name: String,
    var president: String,
    var color: Color,
    var inaugurated: GameDate,
    var ai: Boolean,
) {
    var name: String = if (this.companyId == COMPANY_SPECTATOR) "Spectator" else name
    var bankruptcy: Int = 0
    var passwordProtected: Boolean = false
    var value: BigInteger? = null
    var economy: EconomyState? = null
    var economyHistory = emptyList<PastEconomyState>()
    var shares: IntArray = intArrayOf(INVALID_COMPANY, INVALID_COMPANY, INVALID_COMPANY, INVALID_COMPANY)
    var vehicles: Map<VehicleType, Int> = emptyMap()
    var stations: Map<VehicleType, Int> = emptyMap()

    fun isSpectator(): Boolean {
        return isSpectator(this.companyId)
    }

    fun isSpectator(index: Int): Boolean {
        return index == COMPANY_SPECTATOR
    }

    fun isValid(index: Int): Boolean {
        return index < MAX_COMPANIES
    }

    fun isValidOrSpectator(index: Int): Boolean {
        return isValid(index) || isSpectator(index)
    }

    companion object {
        const val INVALID_COMPANY = 255
        const val COMPANY_SPECTATOR = 255
        const val MAX_COMPANIES = 15L
    }

    enum class VehicleType(val value: Int) {
        TRAIN(0),
        LORRY(1),
        BUS(2),
        PLANE(3),
        SHIP(4);

        companion object {
            private val valuesMap = values().associateBy(VehicleType::value)
            fun valueOf(value: Int) = valuesMap[value]
        }
    }
}
