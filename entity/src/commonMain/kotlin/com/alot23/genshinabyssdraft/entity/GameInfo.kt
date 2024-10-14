package com.alot23.genshinabyssdraft.entity

data class GameInfo(
    val gameToken: String,
    val firstToken: String,
    val secondToken: String,
    val observerToken: String
)

fun String.toGameInfo(): GameInfo {
    val segments = this.split(";")
    return GameInfo(
        gameToken = segments[0],
        firstToken = segments[1],
        secondToken = segments[2],
        observerToken = segments[3]
    )
}