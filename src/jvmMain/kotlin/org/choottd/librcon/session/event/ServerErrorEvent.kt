/*
 * Copyright (c) 2021. Giordano Battilana
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

data class ServerErrorEvent(val error: NetworkErrorCode) : SessionEvent() {

    enum class NetworkErrorCode(val value: Int) {
        UNKNOWN(-1),

        GENERAL(0),  // Try to use this one like never

        /* Signals from clients */
        DESYNC(1),
        SAVEGAME_FAILED(2),
        CONNECTION_LOST(3),
        ILLEGAL_PACKET(4),
        NEWGRF_MISMATCH(5),

        /* Signals from servers */
        NOT_AUTHORIZED(6),
        NOT_EXPECTED(7),
        WRONG_REVISION(8),
        NAME_IN_USE(9),
        WRONG_PASSWORD(10),
        COMPANY_MISMATCH(11),

        // Happens in CLIENT_COMMAND
        KICKED(12),
        CHEATER(13),
        FULL(14);

        companion object {
            private val valuesMap = values().associateBy(NetworkErrorCode::value)
            fun valueOf(value: Int) = valuesMap[value] ?: UNKNOWN
        }

    }
}
