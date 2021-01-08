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

package org.choottd.librcon.session.event

import org.choottd.librcon.session.event.data.CompanyData

data class CompanyRemoveEvent(
    val companyData: CompanyData,
    val removeReason: AdminCompanyRemoveReason
) : SessionEvent() {

    enum class AdminCompanyRemoveReason(val value: Int) {
        UNKNOWN(-1),
        MANUAL(0),
        AUTOCLEAN(1),
        BANKRUPT(2);

        companion object {
            private val valuesMap = values().associateBy(AdminCompanyRemoveReason::value)
            fun valueOf(value: Int) = valuesMap[value] ?: UNKNOWN
        }
    }
}
