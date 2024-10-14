package com.alot23.genshinabyssdraft.presentation

import com.alot23.genshinabyssdraft.entity.Step
import com.alot23.genshinabyssdraft.entity.Role
import com.alot23.genshinabyssdraft.ui.CharacterInfo
import com.alot23.genshinabyssdraft.ui.CharacterSelectionInfo
import com.alot23.genshinabyssdraft.ui.TimerState

data class GameState(
    val role: Role = Role.Observer,
    val selectedCharacter: CharacterInfo? = null,
    val currentStep: Int = 0,
    val gameConfig: List<Step> = emptyList(),
    val firstBans: List<CharacterSelectionInfo> = emptyList(),
    val firstPicks: List<CharacterSelectionInfo> = emptyList(),
    val firstCharacters: List<CharacterInfo> = emptyList(),
    val secondBans: List<CharacterSelectionInfo> = emptyList(),
    val secondPicks: List<CharacterSelectionInfo> = emptyList(),
    val secondCharacters: List<CharacterInfo> = emptyList(),
    val immuneCharacters: List<String> = emptyList(),
    val currentTimer: TimerState?,
    val firstTimer: TimerState,
    val secondTimer: TimerState,
    val showReady: Boolean = true
)