/*
 * Copyright (c) 2020. Giordano Battilana
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
import kotlin.math.floor

object GameStateService {

    fun convertDate(date: Long): GameDate {
        /* There are 97 leap years in 400 years */
        var year = (400 * floor((date / (365 * 400 + 97)).toDouble())).toInt()
        var rem: Int = (date % (365 * 400 + 97)).toInt()

        /* There are 24 leap years in 100 years */
        year += (100 * floor((rem / (365 * 100 + 24)).toDouble())).toInt()
        rem = (rem % (365 * 100 + 24))

        /* There is 1 leap year every 4 years */
        year += (4 * floor((rem / (365 * 4 + 1)).toDouble())).toInt()
        rem = (rem % (365 * 4 + 1))
        while (rem >= if (isLeapYear(year.toDouble())) 366 else 365) {
            rem -= if (isLeapYear(year.toDouble())) 366 else 365
            year++
        }

        val day = ++rem

        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.DAY_OF_YEAR] = day
        return GameDate(year, cal[Calendar.MONTH] + 1, cal[Calendar.DAY_OF_MONTH])
    }

    fun isLeapYear(year: Double): Boolean {
        return year % 4 == 0.0 && (year % 100 != 0.0 || year % 400 == 0.0)
    }
}
