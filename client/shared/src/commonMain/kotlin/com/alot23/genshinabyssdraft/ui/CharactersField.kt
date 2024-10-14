package com.alot23.genshinabyssdraft.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CharactersField(characters: List<CharacterInfo>, clickable: Boolean = false, onCharacterClick: ((CharacterInfo) -> Unit)) = Row {
    val elementCharacters = characters.groupBy { it.character.element }
    for (elementGroup in elementCharacters) {
        val sortedCharacters = elementGroup.value.sortedByDescending { it.level * it.character.rare }
        Column {
            for (elementCharacter in sortedCharacters) {
                Character(characterInfo = elementCharacter, clickable = clickable, onClick = onCharacterClick)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}