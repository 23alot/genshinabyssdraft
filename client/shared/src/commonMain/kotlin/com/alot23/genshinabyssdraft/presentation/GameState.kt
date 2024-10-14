package com.alot23.genshinabyssdraft.presentation

import com.alot23.genshinabyssdraft.entity.Step
import com.alot23.genshinabyssdraft.entity.Role
import com.alot23.genshinabyssdraft.ui.CharacterInfo
import com.alot23.genshinabyssdraft.ui.CharacterSelectionInfo
import com.alot23.genshinabyssdraft.ui.TimerState

data class GameState(
    val selectedCharacter: CharacterInfo? = null,
    val currentStep: Int = 0,
    val firstSelection: List<CharacterSelectionInfo> = emptyList(),
    val secondSelection: List<CharacterSelectionInfo> = emptyList(),
    val firstCharacters: List<CharacterInfo> = emptyList(),
    val secondCharacters: List<CharacterInfo> = emptyList(),
    val timer: Timer,
    val draftSetup: DraftSetup = DraftSetup(),
    val showReady: Boolean = true
)

data class DraftSetup(
    val role: Role = Role.Observer,
    val gameConfig: List<Step> = emptyList(),
    val firstCharacters: List<CharacterInfo> = emptyList(),
    val secondCharacters: List<CharacterInfo> = emptyList(),
    val immuneCharacters: List<String> = emptyList()
)

data class Timer(
    val state: TimerState,
    val firstLeft: Long,
    val secondLeft: Long
)