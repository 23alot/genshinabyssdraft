package com.alot23.genshinabyssdraft.entity

sealed interface Role {

    data object First : Role

    data object Second : Role

    data object Observer : Role

}

fun String.toRole(): Role = when (this) {
    Role.First.toString() -> Role.First
    Role.Second.toString() -> Role.Second
    Role.Observer.toString() -> Role.Observer
    else -> throw IllegalArgumentException("Unknown role")
}