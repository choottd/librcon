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

import java.math.BigInteger
import java.util.*

/**
 * Packets sent to the OpenTTD server
 */
class OutputPacket private constructor(buffer: ByteArray) : Packet(buffer) {

    val dataArray: ByteArray
        get() = buffer

    internal class Builder(type: PacketType) {
        private val buffer: ByteArray = ByteArray(SEND_MTU)
        private var position = POS_PACKET_TYPE + 1

        init {
            buffer[POS_PACKET_TYPE] = type.value.toByte()
        }

        fun writeBool(value: Boolean): Builder {
            buffer[position++] = (if (value) 1 else 0).toByte()
            return this
        }

        fun writeString(value: String): Builder {
            val b = value.toByteArray()
            for (i in b.indices) {
                buffer[position++] = b[i]
            }
            buffer[position++] = '\u0000'.toByte()
            return this
        }

        fun writeUint8(value: Short): Builder {
            buffer[position++] = value.toByte()
            return this
        }

        fun writeUint8(value: Int): Builder {
            this.writeUint8(value.toShort())
            return this
        }

        fun writeUint16(value: Int): Builder {
            buffer[position++] = value.toByte()
            buffer[position++] = (value shr 8).toByte()
            return this
        }

        fun writeUint32(value: Long): Builder {
            buffer[position++] = value.toByte()
            buffer[position++] = (value shr 8).toByte()
            buffer[position++] = (value shr 16).toByte()
            buffer[position++] = (value shr 24).toByte()
            return this
        }

        fun writeUint64(value: Long): Builder {
            buffer[position++] = value.toByte()
            buffer[position++] = (value shr 8).toByte()
            buffer[position++] = (value shr 16).toByte()
            buffer[position++] = (value shr 24).toByte()
            buffer[position++] = (value shr 32).toByte()
            buffer[position++] = (value shr 40).toByte()
            buffer[position++] = (value shr 48).toByte()
            buffer[position++] = (value shr 56).toByte()
            return this
        }

        fun writeUint64(n: BigInteger): Builder {
            this.writeUint64(n.toLong())
            return this
        }

        fun build(): OutputPacket {
            this.buffer[0] = position.toByte()
            this.buffer[1] = (position shr 8).toByte()
            return OutputPacket(buffer.copyOf(position))
        }
    }

}
