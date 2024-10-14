package com.alot23.genshinabyssdraft.presentation.entry

import com.alot23.genshinabyssdraft.entity.GameInfo
import com.alot23.genshinabyssdraft.entity.LogInfo
import kotlinx.serialization.json.JsonObject

sealed interface EntryState {

    data object Login : EntryState

    data class Create(
        val configurations: List<CharactersConfiguration>? = null
    ) : EntryState

    data class LoggedIn(val logInfo: LogInfo) : EntryState

    data class Created(val gameInfo: GameInfo) : EntryState

}

data class CharactersConfiguration(
    val id: String,
    val name: String,
    val items: JsonObject
)