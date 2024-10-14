package com.alot23.genshinabyssdraft.entity

data class LogInfo(
    val gameToken: String,
    val userToken: String,
    val role: Role
)

fun String.toLogInfo(): LogInfo {
    val segments = this.split(";")
    return LogInfo(
        gameToken = segments[0],
        userToken = segments[1],
        role = segments[2].toRole()
    )
}