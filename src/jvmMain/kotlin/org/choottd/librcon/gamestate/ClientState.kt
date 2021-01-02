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

class ClientState(
    val clientId: Long,
    var name: String,
    var companyId: Int,
    val language: NetworkLanguage,
    val address: String,
    val joinDate: GameDate
) {

    companion object {
        const val INVALID_CLIENT_ID = 0
        const val CLIENT_ID_SERVER = 1
    }

    enum class NetworkLanguage(val value: Int) {
        ANY(0),
        ENGLISH(1),
        GERMAN(2),
        FRENCH(3),
        BRAZILIAN(4),
        BULGARIAN(5),
        CHINESE(6),
        CZECH(7),
        DANISH(8),
        DUTCH(9),
        ESPERANTO(10),
        FINNISH(11),
        HUNGARIAN(12),
        ICELANDIC(13),
        ITALIAN(14),
        JAPANESE(15),
        KOREAN(16),
        LITHUANIAN(17),
        NORWEGIAN(18),
        POLISH(19),
        PORTUGUESE(20),
        ROMANIAN(21),
        RUSSIAN(22),
        SLOVAK(23),
        SLOVENIAN(24),
        SPANISH(25),
        SWEDISH(26),
        TURKISH(27),
        UKRAINIAN(28),
        AFRIKAANS(29),
        CROATIAN(30),
        CATALAN(31),
        ESTONIAN(32),
        GALICIAN(33),
        GREEK(34),
        LATVIAN(35),
        COUNT(36);

        companion object {
            private val valuesMap = values().associateBy(NetworkLanguage::value)
            fun valueOf(value: Int) = valuesMap[value] ?: ANY
        }
    }
}
