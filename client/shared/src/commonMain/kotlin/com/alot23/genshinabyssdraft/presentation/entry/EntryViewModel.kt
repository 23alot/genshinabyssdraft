package com.alot23.genshinabyssdraft.presentation.entry

import co.touchlab.kermit.Logger
import com.alot23.genshinabyssdraft.Server
import com.alot23.genshinabyssdraft.entity.CharacterConfiguration
import com.alot23.genshinabyssdraft.entity.GameConfiguration
import com.alot23.genshinabyssdraft.entity.LogInfo
import com.alot23.genshinabyssdraft.entity.Step
import com.alot23.genshinabyssdraft.entity.toGameInfo
import com.alot23.genshinabyssdraft.entity.toLogInfo
import com.alot23.genshinabyssdraft.entity.toRole
import com.alot23.genshinabyssdraft.ui.testGameList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.koin.core.component.KoinComponent

class EntryViewModel() : ViewModel(), KoinComponent {

    companion object {
        const val HOST = Server.Link
        const val CREATE_PATH = "/creategame"
        const val LOGIN_PATH = "/role"
    }

    val state = MutableStateFlow<EntryState>(EntryState.Login)

    private val client = HttpClient() {
        install(WebSockets) {
            pingInterval = 20_000
        }
    }

    fun onConfirmLogin(loginToken: String) {
        val (gameToken, playerToken) = loginToken.split(";")
        viewModelScope.launch {
            val response = client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = HOST
                    path("$LOGIN_PATH/$gameToken/$playerToken")
                }
            }
            val role = response.bodyAsText().toRole()
            val logInfo = LogInfo(
                gameToken = gameToken,
                userToken = playerToken,
                role = role
            )
            state.update {
                EntryState.LoggedIn(logInfo = logInfo)
            }
        }
    }

    fun onCreateClick() = state.update { EntryState.Create() }

    fun onLoginClick() = state.update { EntryState.Login }

    fun applyConfigs(configs: String) {
        val result = Json.parseToJsonElement(configs)
        val configurations = result.jsonArray.map { it.jsonObject }.mapIndexed { index, element ->
            CharactersConfiguration(
                id = "${element.get("title").toString()}$index",
                name = element.get("title").toString(),
                items = element.get("items")!!.jsonObject
            )
        }
        state.update { previousState ->
            EntryState.Create(configurations = configurations)
        }
    }

    fun onConfirmCreate(first: CharactersConfiguration, second: CharactersConfiguration, gameConfigurationRaw: String, immuneCharactersRaw: String) {
        val firstConfig = Json.decodeFromString<List<CharacterConfiguration>>(first.items.toString().normalizeJson())
        val secondConfig = Json.decodeFromString<List<CharacterConfiguration>>(second.items.toString().normalizeJson())
        val gameConfig = Json.decodeFromString<List<String>>(gameConfigurationRaw)
        val immuneCharacters = immuneCharactersRaw.split(";")
        val game = GameConfiguration(firstConfig, secondConfig, gameConfig, immuneCharacters)
        viewModelScope.launch {
            val response = client.post {
                url {
                    protocol = URLProtocol.HTTPS
                    host = HOST
                    path(CREATE_PATH)
                }
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToJsonElement(game).toString())
            }
            val text = response.body<String>()
            Logger.d { "RESPONSE: $text"}
            state.update {
                EntryState.Created(gameInfo = text.toGameInfo())
            }
        }
    }

    private fun String.normalizeJson(): String {
        val raw = replaceFirst("{", "[").dropLast(1) + "]"
        val regex = Regex(pattern = "\"[0-9]+\":[{]")
        val nameRegex = Regex(pattern = "[0-9]+")
        val levelRegex = Regex(pattern = "level\":\"\"")
        val firstTry =  regex.replace(input = raw) { matchResult ->
            val name = nameRegex.find(matchResult.value)?.value ?: "Error"
            return@replace "{\"name\":\"$name\","
        }
        return levelRegex.replace(input = firstTry, "level\":0")
    }

}