package com.alot23.genshinabyssdraft


import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alot23.genshinabyssdraft.entity.Role
import com.alot23.genshinabyssdraft.entity.Step
import com.alot23.genshinabyssdraft.entity.toRole
import com.alot23.genshinabyssdraft.presentation.GameState
import com.alot23.genshinabyssdraft.presentation.HomeViewModel
import com.alot23.genshinabyssdraft.ui.CharacterSelectionArea
import com.alot23.genshinabyssdraft.ui.CharactersField
import com.alot23.genshinabyssdraft.ui.DataScreen
import com.alot23.genshinabyssdraft.ui.EntryScreen
import com.alot23.genshinabyssdraft.ui.Steps
import com.alot23.genshinabyssdraft.ui.Timer
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.query
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun CommonApp() {
    PreComposeApp {
        val navigator = rememberNavigator()
        MaterialTheme {
            NavHost(
                navigator = navigator,
                initialRoute = "/entry"
            ) {
                DataScreen(navigator = navigator)
                EntryScreen(navigator = navigator)
//                navigator.navigate(route = "/greeting/$name")
                scene(route = "/home") { backStackEntry ->
                    val gameToken = backStackEntry.query<String>("gameToken") ?: return@scene navigator.goBack()
                    val playerToken = backStackEntry.query<String>("playerToken") ?: return@scene navigator.goBack()
                    val role = backStackEntry.query<String>("role")?.toRole() ?: return@scene navigator.goBack()
                    val homeViewModel = viewModel(modelClass = HomeViewModel::class) {
                        HomeViewModel(gameToken = gameToken, playerToken = playerToken, role = role)
                    }
                    val state by homeViewModel.state.collectAsStateWithLifecycle()
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row {
                            CharacterSelectionArea(bans = state.firstBans.reversed(), picks = state.firstPicks.reversed(), horizontalAlignment = Alignment.End)
                            state.currentTimer?.let { timer ->
                                Timer(state = timer)
                            }
                            CharacterSelectionArea(bans = state.secondBans, picks = state.secondPicks, horizontalAlignment = Alignment.Start)
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        if (state.gameConfig.isNotEmpty()) {
                            Row {
                                Text(text = state.selectedCharacter?.character?.name ?: "")
                                if (state.isConfirmButtonVisible()) {
                                    Button(
                                        onClick = { homeViewModel.onConfirmButtonClick() }
                                    ) {
                                        Text(text = state.confirmButtonText())
                                    }
                                }
                            }
                        }
                        if (state.showReady) {
                            Button(
                                onClick = {
                                    homeViewModel.onReady()
                                }
                            ) {
                                Text(text = "Ready")
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                        val scrollState = rememberScrollState()
                        Row(modifier = Modifier.verticalScroll(scrollState)) {
                            CharactersField(characters = state.firstCharacters, clickable = state.role is Role.First, onCharacterClick = { character -> homeViewModel.onCharacterClick(character) })
                            Steps(steps = state.gameConfig, step = state.currentStep)
                            CharactersField(characters = state.secondCharacters, clickable = state.role is Role.Second, onCharacterClick = { character -> homeViewModel.onCharacterClick(character) })
                        }

                    }
                }
                scene(route = "/greeting/{name}") { backStackEntry ->
                    backStackEntry.path<String>("name")?.let { name ->
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.h6
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Button(onClick = navigator::goBack) {
                                Text(text = "GO BACK!")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun GameState.isConfirmButtonVisible(): Boolean = when (role) {
    Role.First -> when (gameConfig[currentStep]) {
        is Step.FirstBan,
        is Step.FirstImmune,
        is Step.FirstPick -> selectedCharacter != null
        is Step.Ready,
        is Step.End,
        is Step.SecondBan,
        is Step.SecondImmune,
        is Step.SecondPick -> false
    }
    Role.Second -> when (gameConfig[currentStep]) {
        is Step.Ready,
        is Step.End,
        is Step.FirstBan,
        is Step.FirstImmune,
        is Step.FirstPick -> false
        is Step.SecondBan,
        is Step.SecondImmune,
        is Step.SecondPick -> selectedCharacter != null
    }
    Role.Observer -> false
}

private fun GameState.confirmButtonText(): String = when (role) {
    Role.First -> when (gameConfig[currentStep]) {
        is Step.FirstBan -> "Ban"
        is Step.FirstImmune,
        is Step.FirstPick -> "Pick"
        is Step.Ready,
        is Step.End,
        is Step.SecondBan,
        is Step.SecondImmune,
        is Step.SecondPick -> "Wait"
    }
    Role.Second -> when (gameConfig[currentStep]) {
        is Step.Ready,
        is Step.End,
        is Step.FirstBan,
        is Step.FirstImmune,
        is Step.FirstPick -> "Wait"
        is Step.SecondBan -> "Ban"
        is Step.SecondImmune,
        is Step.SecondPick -> "Pick"
    }
    Role.Observer -> ""
}
