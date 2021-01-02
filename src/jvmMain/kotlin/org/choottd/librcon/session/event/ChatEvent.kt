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

import org.choottd.librcon.gamestate.ClientState
import java.math.BigInteger

data class ChatEvent(
    val action: NetworkAction,
    val destType: DestType,
    val client: ClientState,
    val message: String,
    val data: BigInteger
) : SessionEvent() {

    enum class DestType(val value: Int) {
        UNKNOWN(-1),
        BROADCAST(0),
        TEAM(1),
        CLIENT(2);

        companion object {
            private val valuesMap = values().associateBy(DestType::value)
            fun valueOf(value: Int) = valuesMap[value] ?: UNKNOWN
        }
    }

    enum class NetworkAction(val value: Int) {
        UNKNOWN(-1),
        JOIN(0),
        LEAVE(1),
        SERVER_MESSAGE(2),
        CHAT(3),
        CHAT_COMPANY(4),
        CHAT_CLIENT(5),
        GIVE_MONEY(6),
        NAME_CHANGE(7),
        COMPANY_SPECTATOR(8),
        COMPANY_JOIN(9),
        COMPANY_NEW(10);

        companion object {
            private val valuesMap = values().associateBy(NetworkAction::value)
            fun valueOf(value: Int) = valuesMap[value] ?: UNKNOWN
        }
    }

}
