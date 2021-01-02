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

package org.choottd.librcon.session

import org.choottd.librcon.gamestate.GameState
import org.choottd.librcon.gamestate.GameStateService
import org.choottd.librcon.gamestate.MapState
import org.choottd.librcon.packet.data.GameStatePacketData
import org.choottd.librcon.packet.data.ServerDate
import org.choottd.librcon.packet.data.ServerWelcome
import org.choottd.librcon.session.event.GameData
import org.choottd.librcon.session.event.GameDateEvent
import org.choottd.librcon.session.event.GameUpdateEvent

/**
 * Coroutine that manages the game state
 */
suspend fun Session.gameStateHandler(data: GameStatePacketData) {
    val event = when (data) {

        is ServerWelcome -> {
            val map = MapState(data.mapName, MapState.Landscape.valueOf(data.mapLandscape),
                GameStateService.convertDate(data.mapDateStart), data.mapSeed, data.mapHeight, data.mapWidth)
            val game = GameState(data.gameName, data.gameVersion, data.gameDedicated, map)
            globalState.gameState = game
            GameUpdateEvent(GameData.from(game))
        }

        is ServerDate -> {
            val date = GameStateService.convertDate(data.gameDate)
            globalState.dateCurrent = date
            GameDateEvent(date)
        }
    }

    sendEvent(event)
}
