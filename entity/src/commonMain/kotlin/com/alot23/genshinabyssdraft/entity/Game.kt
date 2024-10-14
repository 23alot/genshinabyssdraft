package com.alot23.genshinabyssdraft.entity

import kotlinx.serialization.Serializable

class Game {
}

@Serializable
data class Step(
    val role: Role,
    val action: Action
)

@Serializable
sealed interface Action {

    @Serializable
    data object Ready : Action {
        override fun toString(): String = "Ready"
    }

    @Serializable
    data object Ban : Action {
        override fun toString(): String = "Ban"
    }

    @Serializable
    data object Pick : Action {
        override fun toString(): String = "Pick"
    }

    @Serializable
    data object Immune : Action {
        override fun toString(): String = "Immune"
    }

    @Serializable
    data object End : Action {
        override fun toString(): String = "End"
    }

}