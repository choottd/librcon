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

package org.choottd.librcon.packet

enum class OutputPacketType(
    val value: Int
) {
    ADMIN_JOIN(0),  ///< The admin announces and authenticates itself to the server.
    ADMIN_QUIT(1),  ///< The admin tells the server that it is quitting.
    ADMIN_UPDATE_FREQUENCY(2),  ///< The admin tells the server the update frequency of a particular piece of information.
    ADMIN_POLL(3),  ///< The admin explicitly polls for a piece of information.
    ADMIN_CHAT(4),  ///< The admin sends a chat message to be distributed.
    ADMIN_RCON(5),  ///< The admin sends a remote console command.
    ADMIN_GAMESCRIPT(6),  ///< The admin sends a JSON string for the GameScript.
    ADMIN_PING(7);  ///< The admin sends a ping to the server, expecting a ping-reply (PONG) packet.

    companion object {
        private val valuesMap = values().associateBy(OutputPacketType::value)
        fun valueOf(value: Int) = valuesMap[value]
    }
}
