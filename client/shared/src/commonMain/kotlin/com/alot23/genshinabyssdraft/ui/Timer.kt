package com.alot23.genshinabyssdraft.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle

@Composable
fun Timer(state: TimerState) = Column {
    val tempState = state.tempState.collectAsStateWithLifecycle()
    val permState = state.permState.collectAsStateWithLifecycle()
    Text(text = tempState.value.toTime())
    Text(text = permState.value.toTime())
}

private fun Int.toTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    val minutesStr = if (minutes < 10) "0$minutes" else "$minutes"
    val secondsStr = if (seconds < 10) "0$seconds" else "$seconds"
    return "$minutesStr:$secondsStr"
}

class TimerState(
    val initialTemp: Int,
    val initialPerm: Int
) {
    val tempState = MutableStateFlow(initialTemp)
    val permState = MutableStateFlow(initialPerm)
    private val executionState = MutableStateFlow(false)

    suspend fun start() {
        executionState.update { true }
        while (tempState.value > 0 && executionState.value) {
            delay(1000L)
            tempState.update { time -> time - 1 }
        }
        while (permState.value > 0 && executionState.value) {
            delay(1000L)
            permState.update { time -> time - 1 }
        }
    }
    fun stop() {
        executionState.update { false }
        tempState.update { initialTemp }
    }
}