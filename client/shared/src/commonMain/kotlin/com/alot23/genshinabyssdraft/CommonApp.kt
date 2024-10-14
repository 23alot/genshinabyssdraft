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
import com.alot23.genshinabyssdraft.entity.Action
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
                            Timer(state = state.timer.state)
                            Column {
                                CharacterSelectionArea(characters = state.firstSelection)
                                CharacterSelectionArea(characters = state.secondSelection)
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        if (state.draftSetup.gameConfig.isNotEmpty()) {
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
                        if (state.draftSetup.gameConfig.isNotEmpty()) {
                            Row(modifier = Modifier.verticalScroll(scrollState)) {
                                CharactersField(
                                    characters = state.firstCharacters,
                                    clickable = state.draftSetup.gameConfig[state.currentStep].toAreaSelectable(ownerRole = Role.First, role = state.draftSetup.role),
                                    onCharacterClick = { character ->
                                        homeViewModel.onCharacterClick(character)
                                    })
                                Steps(steps = state.draftSetup.gameConfig, step = state.currentStep)
                                CharactersField(
                                    characters = state.secondCharacters,
                                    clickable = state.draftSetup.gameConfig[state.currentStep].toAreaSelectable(ownerRole = Role.Second, role = state.draftSetup.role),
                                    onCharacterClick = { character ->
                                        homeViewModel.onCharacterClick(character)
                                    })
                            }
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

// ownerRole only First/Second
private fun Step.toAreaSelectable(ownerRole: Role, role: Role): Boolean = when (this.action) {
    Action.Ban -> role != ownerRole && role.isPlayer()
    Action.Pick,
    Action.Immune -> role == ownerRole // && role.isPlayer() unnecessary check
    Action.End,
    Action.Ready -> false
}

private fun Role.isPlayer(): Boolean = when (this) {
    Role.First,
    Role.Second -> true
    Role.None,
    Role.Observer -> false
}

private fun GameState.isConfirmButtonVisible(): Boolean = draftSetup.gameConfig[currentStep].toConfirmButtonVisible(role = draftSetup.role)

private fun Step.toConfirmButtonVisible(role: Role): Boolean = when (this.action) {
    Action.Ban,
    Action.Pick,
    Action.Immune -> role == this.role
    Action.End,
    Action.Ready -> false
}

private fun GameState.confirmButtonText(): String = when (draftSetup.gameConfig[currentStep].action) {
    Action.Ban -> "Ban"
    Action.Immune,
    Action.Pick -> "Pick"
    Action.Ready,
    Action.End -> "Wait"
}
