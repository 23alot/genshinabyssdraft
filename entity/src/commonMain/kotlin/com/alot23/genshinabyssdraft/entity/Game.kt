package com.alot23.genshinabyssdraft.entity

import kotlinx.serialization.Serializable

class Game {
}

@Serializable
sealed interface Step {

    @Serializable
    data object Ready : Step {
        override fun toString(): String = "Ready"
    }

    @Serializable
    data object FirstPick : Step {
        override fun toString(): String = "FirstPick"
    }

    @Serializable
    data object SecondPick : Step {
        override fun toString(): String = "SecondPick"
    }

    @Serializable
    data object FirstBan : Step {
        override fun toString(): String = "FirstBan"
    }

    @Serializable
    data object SecondBan : Step {
        override fun toString(): String = "SecondBan"
    }

    @Serializable
    data object FirstImmune : Step {
        override fun toString(): String = "FirstImmune"
    }

    @Serializable
    data object SecondImmune : Step {
        override fun toString(): String = "SecondImmune"
    }

    @Serializable
    data object End : Step {
        override fun toString(): String = "End"
    }

}

fun String.toStep(): Step = when (this) {
    Step.Ready.toString() -> Step.Ready
    Step.FirstPick.toString() -> Step.FirstPick
    Step.FirstBan.toString() -> Step.FirstBan
    Step.SecondPick.toString() -> Step.SecondPick
    Step.SecondBan.toString() -> Step.SecondBan
    Step.FirstImmune.toString() -> Step.FirstImmune
    Step.SecondImmune.toString() -> Step.SecondImmune
    Step.End.toString() -> Step.End
    else -> throw IllegalArgumentException("Unknown action")
}