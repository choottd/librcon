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

package org.choottd.librcon.packet

enum class PacketType(  ///< An invalid marker for admin packets.
    val value: Int
) {
    ADMIN_JOIN(0),  ///< The admin announces and authenticates itself to the server.
    ADMIN_QUIT(1),  ///< The admin tells the server that it is quitting.
    ADMIN_UPDATE_FREQUENCY(2),  ///< The admin tells the server the update frequency of a particular piece of information.
    ADMIN_POLL(3),  ///< The admin explicitly polls for a piece of information.
    ADMIN_CHAT(4),  ///< The admin sends a chat message to be distributed.
    ADMIN_RCON(5),  ///< The admin sends a remote console command.
    ADMIN_GAMESCRIPT(6),  ///< The admin sends a JSON string for the GameScript.
    ADMIN_PING(7),  ///< The admin sends a ping to the server, expecting a ping-reply (PONG) packet.
    SERVER_FULL(100),  ///< The server tells the admin it cannot accept the admin.
    SERVER_BANNED(101),  ///< The server tells the admin it is banned.
    SERVER_ERROR(102),  ///< The server tells the admin an error has occurred.
    SERVER_PROTOCOL(103),  ///< The server tells the admin its protocol version.
    SERVER_WELCOME(104),  ///< The server welcomes the admin to a game.
    SERVER_NEWGAME(105),  ///< The server tells the admin its going to start a new game.
    SERVER_SHUTDOWN(106),  ///< The server tells the admin its shutting down.
    SERVER_DATE(107),  ///< The server tells the admin what the current game date is.
    SERVER_CLIENT_JOIN(108),  ///< The server tells the admin that a client has joined.
    SERVER_CLIENT_INFO(109),  ///< The server gives the admin information about a client.
    SERVER_CLIENT_UPDATE(110),  ///< The server gives the admin an information update on a client.
    SERVER_CLIENT_QUIT(111),  ///< The server tells the admin that a client quit.
    SERVER_CLIENT_ERROR(112),  ///< The server tells the admin that a client caused an error.
    SERVER_COMPANY_NEW(113),  ///< The server tells the admin that a new company has started.
    SERVER_COMPANY_INFO(114),  ///< The server gives the admin information about a company.
    SERVER_COMPANY_UPDATE(115),  ///< The server gives the admin an information update on a company.
    SERVER_COMPANY_REMOVE(116),  ///< The server tells the admin that a company was removed.
    SERVER_COMPANY_ECONOMY(117),  ///< The server gives the admin some economy related company information.
    SERVER_COMPANY_STATS(118),  ///< The server gives the admin some statistics about a company.
    SERVER_CHAT(119),  ///< The server received a chat message and relays it.
    SERVER_RCON(120),  ///< The server's reply to a remove console command.
    SERVER_CONSOLE(121),  ///< The server gives the admin the data that got printed to its console.
    SERVER_CMD_NAMES(122),  ///< The server gives the admin names of all DoCommands.
    SERVER_CMD_LOGGING(123),  ///< The server gives the admin DoCommand information (for logging purposes only).
    SERVER_GAMESCRIPT(124),  ///< The server gives the admin information from the GameScript in JSON.
    SERVER_RCON_END(125),  ///< The server indicates that the remote console command has completed.
    SERVER_PONG(126),  ///< The server replies to a ping request from the admin.
    INVALID_ADMIN_PACKET(0xFF);

    companion object {
        private val valuesMap = values().associateBy(PacketType::value)
        fun valueOf(value: Int) = valuesMap[value] ?: INVALID_ADMIN_PACKET
    }
}
