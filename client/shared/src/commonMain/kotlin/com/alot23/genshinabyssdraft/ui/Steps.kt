package com.alot23.genshinabyssdraft.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alot23.genshinabyssdraft.entity.Step

@Composable
fun Steps(steps: List<Step>, step: Int) = Column {
    steps.forEachIndexed { index, stepIndexed ->
        Text(text = stepIndexed.toShortString(), textAlign = stepIndexed.toTextAlignment(), fontWeight = if (index == step) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.width(100.dp).fillMaxWidth())
    }
}

private fun Step.toShortString(): String = when (this) {
    is Step.Ready,
    is Step.End -> ""
    is Step.FirstBan,
    is Step.SecondBan -> "Ban"
    is Step.FirstImmune,
    is Step.FirstPick,
    is Step.SecondImmune,
    is Step.SecondPick -> "Pick"
}

private fun Step.toTextAlignment(): TextAlign = when (this) {
    is Step.Ready,
    is Step.End -> TextAlign.Center
    is Step.FirstBan,
    is Step.FirstImmune,
    is Step.FirstPick -> TextAlign.Start
    is Step.SecondBan,
    is Step.SecondImmune,
    is Step.SecondPick -> TextAlign.End
}