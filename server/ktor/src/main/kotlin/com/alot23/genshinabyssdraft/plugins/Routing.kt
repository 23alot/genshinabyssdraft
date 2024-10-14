package com.alot23.genshinabyssdraft.plugins

import com.alot23.genshinabyssdraft.GameStore
import com.alot23.genshinabyssdraft.PlayerSession
import com.alot23.genshinabyssdraft.entity.GameConfiguration
import com.alot23.genshinabyssdraft.entity.GameStep
import com.alot23.genshinabyssdraft.entity.Role
import com.alot23.genshinabyssdraft.entity.Step
import io.ktor.serialization.deserialize
import io.ktor.serialization.serialize
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.websocket.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.websocket.*

fun Application.configureRouting(store: GameStore) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/creategame") {
            val gameConfiguration = call.receive<GameConfiguration>()
            val game = store.createGame(gameConfiguration = gameConfiguration)
            val message = with(game) { "$gameToken;$firstToken;$secondToken;$observerToken" }
            call.respond(message = message)
        }
        get("/role/{game?}/{token?}") {
            val gameToken = call.parameters["game"] ?: return@get call.respond("Error")
            val token = call.parameters["token"] ?: return@get call.respond("Error")
            val game = store.getGame(gameToken = gameToken)
            val message = when (token) {
                game.firstToken -> Role.First.toString()
                game.secondToken -> Role.Second.toString()
                game.observerToken -> Role.Observer.toString()
                else -> return@get call.respond("Error")
            }
            println(message)
            call.respond(message = message)
        }
        get("/configuration/{game?}/{token?}") {
            val gameToken = call.parameters["game"] ?: return@get call.respond("Error")
            val token = call.parameters["token"] ?: return@get call.respond("Error")
            val game = store.getGame(gameToken = gameToken)
            call.respond(game.gameConfiguration)
        }
        webSocket("/echo/{game?}/{token?}") {
            val gameToken = call.parameters["game"] ?: return@webSocket call.respond("Error")
            val token = call.parameters["token"] ?: return@webSocket call.respond("Error")
            val game = store.getGame(gameToken = gameToken)
            store.putPlayerSession(playerToken = token, session = PlayerSession(session = this))
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val converter = converter ?: continue
                val gameStep = converter.deserialize<GameStep>(frame)
                when (gameStep.action) {
                    is Step.Ready -> {
                        store.getPlayerSession(playerToken = gameStep.data)?.isReady = true
                        val firstSession = store.getPlayerSession(playerToken = game.firstToken) ?: continue
                        val secondSession = store.getPlayerSession(playerToken = game.secondToken) ?: continue
                        if (firstSession.isReady && secondSession.isReady) {
                            val observerSession = store.getPlayerSession(playerToken = game.observerToken)
                            val sessions = listOf(firstSession, secondSession, observerSession)
                            sessions.filterNotNull().forEach { session ->
                                if (session.session.closeReason.isActive) {
                                    session.session.send(converter.serialize(gameStep))
                                }
                            }
                        }
                    }
                    is Step.End -> {
                        val firstSession = store.getPlayerSession(playerToken = game.firstToken)
                        val secondSession = store.getPlayerSession(playerToken = game.secondToken)
                        val observerSession = store.getPlayerSession(playerToken = game.observerToken)
                        val sessions = listOf(firstSession, secondSession, observerSession)
                        sessions.filterNotNull().forEach { session ->
                            if (session.session.closeReason.isActive) {
                                session.session.send(converter.serialize(GameStep(0, Step.End, converter.serialize(game.history).toString())))
                            }
                        }
                        store.clearGame(game = game)
                    }
                    else -> {
                        val firstSession = store.getPlayerSession(playerToken = game.firstToken)
                        val secondSession = store.getPlayerSession(playerToken = game.secondToken)
                        val observerSession = store.getPlayerSession(playerToken = game.observerToken)
                        val sessions = listOf(firstSession, secondSession, observerSession)
                        sessions.filterNotNull().forEach { session ->
                            if (session.session.closeReason.isActive) {
                                session.session.send(converter.serialize(gameStep))
                            }
                        }
                    }
                }

            }
        }
    }
}
