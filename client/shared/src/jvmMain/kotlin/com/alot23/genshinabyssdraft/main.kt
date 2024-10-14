package com.alot23.genshinabyssdraft

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

suspend fun main() = application {
    Window(
        title = "Multiplatform App",
        state = rememberWindowState(width = 1000.dp, height = 1000.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(1000, 1000)
        CommonApp()
    }
}