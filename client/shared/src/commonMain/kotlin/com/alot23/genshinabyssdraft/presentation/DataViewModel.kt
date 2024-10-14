package com.alot23.genshinabyssdraft.presentation

import co.touchlab.kermit.Logger
import com.alot23.genshinabyssdraft.entity.CharacterConfiguration
import com.alot23.genshinabyssdraft.entity.GameConfiguration
import com.alot23.genshinabyssdraft.entity.LogInfo
import com.alot23.genshinabyssdraft.entity.toGameInfo
import com.alot23.genshinabyssdraft.entity.toRole
import com.alot23.genshinabyssdraft.presentation.entry.EntryState
import com.alot23.genshinabyssdraft.presentation.entry.EntryViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.koin.core.component.KoinComponent

class DataViewModel() : ViewModel(), KoinComponent {

    companion object {
        const val HOST = "bbs-api-os.hoyoverse.com"
        const val RECORD_PATH = "/game_record/genshin/api/index"
        const val SERVER = "os_euro"
    }

    val state = MutableStateFlow<String>("")

    private val client = HttpClient() {
        install(WebSockets) {
            pingInterval = 20_000
        }
    }

    fun request(uid: String) {
        val result = Json.parseToJsonElement(uid)
        val a = result.jsonArray.joinToString { jsonElement -> jsonElement.jsonObject.get("title").toString()}
        state.update { a }
    }

    @Serializable
    data class JsonTestObject(
        val title: String,
        val items: Item
    )

    @Serializable
    data class Item(
        val map: Map<String, Character>
    )

    @Serializable
    data class Character(
        val level: String,
        val const: String
    )
}