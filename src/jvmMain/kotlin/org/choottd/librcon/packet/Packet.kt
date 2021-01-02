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

abstract class Packet(protected val buffer: ByteArray) {

    override fun toString(): String {
        return buffer.toString()
    }

    companion object {
        const val FF_MASK = 0xFF.toByte()
        const val SEND_MTU = 1460
        const val POS_PACKET_TYPE = 2
    }
}
