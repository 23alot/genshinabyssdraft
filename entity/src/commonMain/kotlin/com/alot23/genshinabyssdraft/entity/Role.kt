package com.alot23.genshinabyssdraft.entity

import kotlinx.serialization.Serializable

@Serializable
sealed interface Role {

    @Serializable
    data object None : Role

    @Serializable
    data object First : Role

    @Serializable
    data object Second : Role

    @Serializable
    data object Observer : Role

}

fun String.toRole(): Role = when (this) {
    Role.First.toString() -> Role.First
    Role.Second.toString() -> Role.Second
    Role.Observer.toString() -> Role.Observer
    Role.None.toString() -> Role.None
    else -> throw IllegalArgumentException("Unknown role")
}