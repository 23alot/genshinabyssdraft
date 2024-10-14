package com.alot23.genshinabyssdraft.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GameConfiguration(
    val firstCharacters: List<CharacterConfiguration>,
    val secondCharacters: List<CharacterConfiguration>,
    val gameConfiguration: List<String>,
    val immuneCharacters: List<String>
)


@Serializable
data class CharacterConfiguration(
    @SerialName(value = "name") val name: String = "",
    @SerialName(value = "level") val level: Int,
    @SerialName(value = "const") val constellations: Int
)
