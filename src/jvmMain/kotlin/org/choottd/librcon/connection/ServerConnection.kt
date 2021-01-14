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
package org.choottd.librcon.connection

import org.choottd.librcon.packet.InputPacket
import org.choottd.librcon.packet.OutputPacket
import org.choottd.librcon.packet.Packet
import java.io.Closeable
import java.net.InetSocketAddress
import java.net.SocketException
import java.net.StandardSocketOptions
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ServerConnection(
    private val host: String,
    private val port: Int
) : Closeable {

    private val socketChannel = AsynchronousSocketChannel.open()
        .setOption(StandardSocketOptions.TCP_NODELAY, true)
        .setOption(StandardSocketOptions.SO_KEEPALIVE, false)

    val isOpen
        get() = socketChannel.isOpen

    suspend fun open(): Unit = suspendCoroutine { cont ->
        socketChannel.connect(InetSocketAddress(host, port), Unit,
            object : CompletionHandler<Void, Unit> {
                override fun completed(result: Void?, attachment: Unit) = cont.resume(Unit)
                override fun failed(exc: Throwable, attachment: Unit) = cont.resumeWithException(exc)
            })
    }

    suspend fun writePacket(packet: OutputPacket) = suspendCoroutine<Unit> { cont ->
        val byteBuffer = ByteBuffer.wrap(packet.dataArray)
        socketChannel.write(byteBuffer, Unit, object : CompletionHandler<Int, Unit> {
            override fun completed(result: Int?, attachment: Unit?) = cont.resume(Unit)
            override fun failed(exc: Throwable, attachment: Unit?) = cont.resumeWithException(exc)
        })
    }

    suspend fun readPacket(): InputPacket {
        if (!socketChannel.isOpen) {
            throw RuntimeException("Socket closed")
        }

        val bufferForLength = ByteArray(2)
        readToBuffer(bufferForLength)
        val length = readLength(bufferForLength)

        if (length > Packet.SEND_MTU) {
            throw IndexOutOfBoundsException("Packet length claims to be greater than SEND_MTU")
        }
        if (length == 0) {
            throw SocketException("Empty packet received")
        }

        val bufferForData = ByteArray(length - 2)
        readToBuffer(bufferForData)
        val buffer = bufferForLength + bufferForData
        return InputPacket(buffer)
    }

    private suspend fun readToBuffer(buffer: ByteArray) = suspendCoroutine<ByteArray> { cont ->
        val byteBuffer = ByteBuffer.wrap(buffer)
        socketChannel.read(byteBuffer, Unit, object : CompletionHandler<Int, Unit> {
            override fun completed(result: Int?, attachment: Unit?) = cont.resume(byteBuffer.array())
            override fun failed(exc: Throwable, attachment: Unit?) = cont.resumeWithException(exc)
        })
    }

    private fun readLength(buffer: ByteArray): Int {
        val b1: Int = (buffer[0].toInt() and Packet.FF_MASK).toInt()
        val b2: Int = (buffer[1].toInt() and Packet.FF_MASK).toInt()
        return b1 + (b2 shl 8)
    }

    override fun close() {
        socketChannel.close()
    }

}
