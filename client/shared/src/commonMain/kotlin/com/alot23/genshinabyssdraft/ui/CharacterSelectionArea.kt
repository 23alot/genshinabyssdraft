package com.alot23.genshinabyssdraft.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

@Composable
fun CharacterSelectionArea(
    characters: List<CharacterSelectionInfo>
) = Row {
    for (character in characters) {
        CharacterSelection(characterSelectionInfo = character)
    }
}