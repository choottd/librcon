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

import java.io.UnsupportedEncodingException
import java.math.BigInteger
import kotlin.experimental.and

/**
 * Packet received from the OpenTTD server
 */
class InputPacket(buffer: ByteArray) : Packet(buffer) {
    private var position = POS_PACKET_TYPE + 1

    val type: PacketType = PacketType.valueOf((buffer[POS_PACKET_TYPE] and FF_MASK).toInt())

    fun readBool(): Boolean {
        return buffer[position++] and FF_MASK > 0
    }

    fun readUint8(): Int {
        return (buffer[position++] and FF_MASK).toInt()
    }

    fun readUint16(): Int {
        var n = (buffer[position++] and FF_MASK).toInt()
        n += (buffer[position++] and FF_MASK).toInt() shl 8
        return n
    }

    fun readUint32(): Long {
        var n: Long = (buffer[position++] and FF_MASK).toLong()
        n += ((buffer[position++] and FF_MASK).toLong() shl 8)
        n += ((buffer[position++] and FF_MASK).toLong() shl 16)
        n += ((buffer[position++] and FF_MASK).toLong() shl 24)
        return n
    }

    fun readUint64(): BigInteger {
        var l: Long = 0
        l += (buffer[position++] and FF_MASK).toLong()
        l += (buffer[position++] and FF_MASK).toLong() shl 8
        l += (buffer[position++] and FF_MASK).toLong() shl 16
        l += (buffer[position++] and FF_MASK).toLong() shl 24
        l += (buffer[position++] and FF_MASK).toLong() shl 32
        l += (buffer[position++] and FF_MASK).toLong() shl 40
        l += (buffer[position++] and FF_MASK).toLong() shl 48
        l += (buffer[position++] and FF_MASK).toLong() shl 56
        return BigInteger.valueOf(l)
    }

    fun readString(): String {
        val startIdx = position
        while (buffer[position++] != '\u0000'.toByte()) {
            // move forward
        };
        val endIdx = position - startIdx - 1
        return try {
            String(buffer, startIdx, endIdx, Charsets.UTF_8)
        } catch (ex: UnsupportedEncodingException) {
            /* as "UTF-8" is a supported encoding, we will ignore this part! */
            ""
        }
    }

    override fun toString(): String {
        return "InputPacket - ${buffer.size} bytes"
    }

}
