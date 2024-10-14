@file:OptIn(ExperimentalResourceApi::class)

package com.alot23.genshinabyssdraft.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alot23.genshinabyssdraft.entity.Action
import com.alot23.genshinabyssdraft.entity.Step
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun CharacterSelection(characterSelectionInfo: CharacterSelectionInfo) {
    val modifier = Modifier.height(height = 80.dp).width(80.dp)
    val borderWidth = 2.dp
    return Box(
        modifier = modifier.border(borderWidth, characterSelectionInfo.borderColor(), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        characterSelectionInfo.characterInfo?.let { info ->
            Image(
                painter = painterResource(info.character.icon),
                contentDescription = null,
                modifier = Modifier.height(height = 72.dp).width(72.dp).clip(shape = CircleShape).background(color = info.character.element.toColor()).align(Alignment.TopCenter)
            )
            Row(modifier = Modifier.clip(shape = RoundedCornerShape(size = 8.dp)).background(color = Color.White).align(Alignment.BottomCenter).height(24.dp)) {
                Text(text = "${info.level}", modifier = Modifier.width(32.dp).padding(vertical = 1.dp, horizontal = 4.dp).align(
                    Alignment.CenterVertically), textAlign = TextAlign.End)
                Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(color = Color.Black))
                Text(text = "${info.constellations}", modifier = Modifier.width(32.dp).padding(vertical = 1.dp, horizontal = 4.dp).align(
                    Alignment.CenterVertically), textAlign = TextAlign.Start)
            }
        } ?: run {
            val backgroundColor = characterSelectionInfo.backgroundColor()
            val borderColor = characterSelectionInfo.borderColor()
            val textModifier = Modifier.height(height = 72.dp).width(72.dp).clip(shape = CircleShape).background(color = backgroundColor).border(borderWidth, borderColor, CircleShape).wrapContentHeight(align = Alignment.CenterVertically)
            Text(text = characterSelectionInfo.index.toString(), modifier = textModifier, textAlign = TextAlign.Center, fontSize = 24.sp)
        }

    }
}

private fun CharacterSelectionInfo.backgroundColor(): Color = when (this.step.action) {
    Action.Ban -> Color.Red
    Action.Immune,
    Action.Pick -> Color.Green
    Action.Ready,
    Action.End -> throw IllegalArgumentException("${step.action} should not be used here")
}

private fun CharacterSelectionInfo.borderColor(): Color = when {
    this.isActive -> Color.Yellow
    else -> this.backgroundColor()
}

data class CharacterSelectionInfo(
    val index: Int,
    val characterInfo: CharacterInfo? = null,
    val step: Step,
    val isActive: Boolean = false
)