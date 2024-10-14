package com.alot23.genshinabyssdraft.data.repository

import co.touchlab.kermit.Logger
import com.alot23.genshinabyssdraft.Server
import com.alot23.genshinabyssdraft.entity.GameStep
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

class RemoteGameRepository : GameRepository {
    private val gameMove: MutableStateFlow<GameStep?> = MutableStateFlow(null)

    override fun isReady(): Boolean = false

    override suspend fun observeGameSteps(gameToken: String, playerToken: String): Flow<List<GameStep>> = flow {
        val PATH = "/echo/$gameToken/$playerToken"
        client.webSocket(
            method = HttpMethod.Get,
            host = Server.Link,
            port = Server.Port,
            path = PATH
        ) {

            val messageOutputRoutine = launch { outputMessages(collector = this@flow) }
            val userInputRoutine = launch { inputMessages() }

            userInputRoutine.join() // Wait for completion; either "exit" or error
            messageOutputRoutine.cancelAndJoin()

        }
    }

    override fun onGameStep(gameStep: GameStep) {
        gameMove.update { gameStep }
    }

    private val client = HttpClient() {
        install(WebSockets) {
            pingInterval = 20_000
        }
        install(ContentNegotiation) {
            json()
        }
    }

    private suspend fun DefaultClientWebSocketSession.outputMessages(collector: FlowCollector<List<GameStep>>) {
        try {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val text = message.readText()
                val gameSteps = Json.decodeFromString<List<GameStep>>(text)
                collector.emit(value = gameSteps)
            }
        } catch (e: Exception) {
            Logger.d { "Error while receiving: ${e.message}" }
        }
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages() {
        while (true) {
            gameMove.collect { move ->
                if (move != null) {
                    outgoing.send(Frame.Text(Json.encodeToJsonElement(move).toString()))
                }
            }
        }
    }
}