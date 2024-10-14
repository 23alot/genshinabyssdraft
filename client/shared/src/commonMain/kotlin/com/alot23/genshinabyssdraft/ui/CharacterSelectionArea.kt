package com.alot23.genshinabyssdraft.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun CharacterSelectionArea(
    bans: List<CharacterSelectionInfo>,
    picks: List<CharacterSelectionInfo>,
    horizontalAlignment: Alignment.Horizontal
) = Column(horizontalAlignment = horizontalAlignment) {
    Row {
        for (info in bans) {
            CharacterSelection(characterSelectionInfo = info)
        }
    }
    Row {
        for (info in picks) {
            CharacterSelection(characterSelectionInfo = info)
        }
    }
}