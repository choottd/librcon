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

import java.util.*


data class GameDate(
    val year: Int,
    val month: Int,
    val day: Int
) {

    constructor(cal: Calendar) : this(cal[Calendar.YEAR], cal[Calendar.MONTH] + 1, cal[Calendar.DAY_OF_MONTH])

    val quarter: Int
        get() = (month + 2) / 3

    fun previousQuarter(): GameDate {
        val cal = Calendar.getInstance()
        cal[year, month] = day
        cal.add(Calendar.MONTH, -3)
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        return GameDate(cal)
    }

    override fun toString() = "$year-$month-$day"
}
