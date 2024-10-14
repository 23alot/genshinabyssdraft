package com.alot23.genshinabyssdraft.entity

import kotlinx.serialization.Serializable

@Serializable
data class GameStep(
    val step: Int,
    val action: Step,
    val data: String
)