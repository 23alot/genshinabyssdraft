package com.alot23.genshinabyssdraft

import com.alot23.genshinabyssdraft.entity.GameConfiguration
import com.alot23.genshinabyssdraft.entity.GameStep
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import java.util.*

class GameStore {
    
    private val gameMap: MutableMap<String, GameSetup> = Collections.synchronizedMap<String, GameSetup>(emptyMap()).toMutableMap()
    private val sessions: MutableMap<String, PlayerSession> = Collections.synchronizedMap<String, PlayerSession>(emptyMap()).toMutableMap()
    
    fun createGame(gameConfiguration: GameConfiguration, gameToken: String = UUID.randomUUID().toString()): GameSetup {
        val firstToken = UUID.randomUUID().toString()
        val secondToken = UUID.randomUUID().toString()
        val observerToken = UUID.randomUUID().toString()
        
        val game = GameSetup(gameToken = gameToken, firstToken = firstToken, secondToken = secondToken, observerToken = observerToken, gameConfiguration = gameConfiguration)
        gameMap[gameToken] = game

        return game
    }
    
    fun getGame(gameToken: String): GameSetup {
        return gameMap[gameToken] ?: throw IllegalArgumentException("No games found")
    }
    
    fun getPlayerSession(playerToken: String): PlayerSession? = sessions[playerToken]
    
    fun putPlayerSession(playerToken: String, session: PlayerSession) {
        sessions[playerToken] = session
    }

    fun clearGame(game: GameSetup) {
        gameMap.remove(game.gameToken)
        sessions.remove(game.firstToken)
        sessions.remove(game.secondToken)
        sessions.remove(game.observerToken)
    }
    
}

@Serializable
data class GameSetup(
    val gameToken: String,
    val firstToken: String,
    val secondToken: String,
    val observerToken: String,
    val gameConfiguration: GameConfiguration,
    val history: MutableList<GameStep> = mutableListOf()
)

data class PlayerSession(
    var session: DefaultWebSocketSession,
    var isReady: Boolean = false
)