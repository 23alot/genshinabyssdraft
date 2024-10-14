package com.alot23.genshinabyssdraft.presentation

import co.touchlab.kermit.Logger
import com.alot23.genshinabyssdraft.Server
import com.alot23.genshinabyssdraft.entity.CharacterConfiguration
import com.alot23.genshinabyssdraft.entity.GameConfiguration
import com.alot23.genshinabyssdraft.entity.Step
import com.alot23.genshinabyssdraft.entity.GameStep
import com.alot23.genshinabyssdraft.entity.Role
import com.alot23.genshinabyssdraft.entity.toStep
import com.alot23.genshinabyssdraft.ui.CharacterInfo
import com.alot23.genshinabyssdraft.ui.CharacterSelectionInfo
import com.alot23.genshinabyssdraft.ui.TimerState
import com.alot23.genshinabyssdraft.ui.toCharacterInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.koin.core.component.KoinComponent
import kotlin.random.Random

class HomeViewModel(
    private val gameToken: String,
    private val playerToken: String,
    private val role: Role
) : ViewModel(), KoinComponent {

    private val PATH = "/echo/$gameToken/$playerToken"
    private val CONFIGURATION_PATH = "/configuration/$gameToken/$playerToken"
    private val gameMove: MutableStateFlow<GameStep?> = MutableStateFlow(null)
    private val firstTimer: TimerState = TimerState(20, 180)
    private val secondTimer: TimerState = TimerState(20, 180)

    val state = MutableStateFlow(
        GameState(
            role = role,
            currentTimer = firstTimer,
            firstTimer = firstTimer,
            secondTimer = secondTimer
        )
    )

    private val client = HttpClient() {
        install(WebSockets) {
            pingInterval = 20_000
        }
        install(ContentNegotiation) {
            json()
        }
    }
    init {
        init()
    }

    private fun init() {
        setupConfiguration()
        viewModelScope.launch {
            client.webSocket(method = HttpMethod.Get, host = Server.Link, port = Server.Port, path = PATH) {

                val messageOutputRoutine = launch { outputMessages() }
                val userInputRoutine = launch { inputMessages() }

                userInputRoutine.join() // Wait for completion; either "exit" or error
                messageOutputRoutine.cancelAndJoin()
            }

        }
    }

    private fun setupConfiguration() {
        viewModelScope.launch {
            val response = client.get {
                url {
                    protocol = URLProtocol.HTTP
                    host = Server.Link
                    port = Server.Port
                    path(CONFIGURATION_PATH)
                }
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
            val gameConfiguration = response.body<GameConfiguration>()
            val gameConfig = gameConfiguration.gameConfiguration.map { it.toStep() }
            val firstCharacters = gameConfiguration.firstCharacters.removeUnusedCharacters().map { it.toCharacterInfo(immuneCharacters = gameConfiguration.immuneCharacters) }
            val secondCharacters = gameConfiguration.secondCharacters.removeUnusedCharacters().map { it.toCharacterInfo(immuneCharacters = gameConfiguration.immuneCharacters) }
            val indexedSteps = gameConfig.toIndexed()
            state.update { previousState ->
                return@update previousState.copy(
                    firstCharacters = firstCharacters,
                    secondCharacters = secondCharacters,
                    gameConfig = gameConfig,
                    firstBans = indexedSteps.toFirstBans(),
                    firstPicks = indexedSteps.toFirstPicks(),
                    secondBans = indexedSteps.toSecondBans(),
                    secondPicks = indexedSteps.toSecondPicks(),
                    immuneCharacters = gameConfiguration.immuneCharacters
                )
            }
        }
    }

    private fun List<CharacterConfiguration>.removeUnusedCharacters(): List<CharacterConfiguration> = filterNot { configuration -> configuration.level <= 0 }

    private suspend fun DefaultClientWebSocketSession.outputMessages() {
        try {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val text = message.readText()
                val gameStep = Json.decodeFromString<GameStep>(text)
                onGameStep(gameStep = gameStep)
            }
        } catch (e: Exception) {
            Logger.d { "Error while receiving: ${e.message}" }
        }
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages() {
        while (true) {
            gameMove.collect { move ->
                if (move != null) {
                    outgoing.send(Frame.Text(Json.encodeToJsonElement(move).toString()))
                }
            }
        }
    }

    fun onReady() {
        gameMove.update { GameStep(0, Step.Ready, data = playerToken) }
    }

    fun onConfirmButtonClick() {
        state.update { previousState ->
            val currentStep = previousState.currentStep
            val stepState = previousState.gameConfig[currentStep]
            val character = previousState.selectedCharacter ?: return@update previousState
            gameMove.update { GameStep(step = currentStep, action = stepState, data = character.character.name) }
            return@update previousState
        }
    }

    fun onCharacterClick(character: CharacterInfo) = state.update { previousState ->
        return@update previousState.copy(selectedCharacter = character)
    }

    private fun onGameStep(gameStep: GameStep) = state.update { previousState ->
        firstTimer.stop()
        secondTimer.stop()
        val stepState = previousState.gameConfig[gameStep.step]
        val nextState = previousState.gameConfig[gameStep.step + 1]
        val firstCharacter = previousState.firstCharacters.find { info -> info.character.name == gameStep.data }
        val secondCharacter = previousState.secondCharacters.find { info -> info.character.name == gameStep.data }
        val newFirstCharacters = previousState.firstCharacters.removeFirstFromField(step = stepState, character = firstCharacter).updateFirstImmuneField(step = nextState, immuneCharacters = previousState.immuneCharacters)
        val newSecondCharacters = previousState.secondCharacters.removeSecondFromField(step = stepState, character = secondCharacter).updateSecondImmuneField(step = nextState, immuneCharacters = previousState.immuneCharacters)
        val newTimer = nextState.getNewTimer()
        viewModelScope.launch { newTimer?.start() }
        previousState.copy(
            selectedCharacter = null,
            currentStep = previousState.currentStep + 1,
            firstBans = previousState.firstBans.updateFirstBans(step = stepState, character = firstCharacter).updateFirstBansSelection(step = nextState),
            firstPicks = previousState.firstPicks.updateFirstPicks(step = stepState, character = firstCharacter).updateFirstPicksSelection(step = nextState),
            firstCharacters = newFirstCharacters,
            secondCharacters = newSecondCharacters,
            secondBans = previousState.secondBans.updateSecondBans(step = stepState, character = secondCharacter).updateSecondBansSelection(step = nextState),
            secondPicks = previousState.secondPicks.updateSecondPicks(step = stepState, character = secondCharacter).updateSecondPicksSelection(step = nextState),
            currentTimer = newTimer,
            showReady = false
        )
    }

    private fun Step.getNewTimer(): TimerState? = when (this) {
        is Step.FirstBan,
        is Step.FirstImmune,
        is Step.FirstPick -> firstTimer
        is Step.SecondBan,
        is Step.SecondImmune,
        is Step.SecondPick -> secondTimer
        is Step.Ready,
        is Step.End -> null
    }

    private fun List<CharacterInfo>.removeFirstFromField(step: Step, character: CharacterInfo?): List<CharacterInfo> = when (step) {
        is Step.FirstBan,
        is Step.FirstImmune,
        is Step.FirstPick,
        is Step.SecondBan,
        is Step.SecondPick -> if (character != null) this.minusElement(element = character) else this
        is Step.Ready,
        is Step.End,
        is Step.SecondImmune -> this
    }

    private fun List<CharacterInfo>.updateFirstImmuneField(step: Step, immuneCharacters: List<String>): List<CharacterInfo> = when (step) {
        is Step.FirstImmune -> enableImmuneCharacters()
        is Step.FirstBan,
        is Step.FirstPick,
        is Step.SecondBan,
        is Step.SecondPick,
        is Step.Ready,
        is Step.End,
        is Step.SecondImmune -> disableImmuneCharacters(immuneCharacters = immuneCharacters)
    }

    private fun List<CharacterInfo>.updateSecondImmuneField(step: Step, immuneCharacters: List<String>): List<CharacterInfo> = when (step) {
        is Step.SecondImmune -> enableImmuneCharacters()
        is Step.FirstBan,
        is Step.FirstPick,
        is Step.SecondBan,
        is Step.SecondPick,
        is Step.Ready,
        is Step.End,
        is Step.FirstImmune -> disableImmuneCharacters(immuneCharacters = immuneCharacters)
    }

    private fun List<CharacterInfo>.disableImmuneCharacters(immuneCharacters: List<String>): List<CharacterInfo> = map { characterInfo -> characterInfo.copy(isDisabled = characterInfo.character.name in immuneCharacters) }
    private fun List<CharacterInfo>.enableImmuneCharacters(): List<CharacterInfo> = map { characterInfo -> characterInfo.copy(isDisabled = false) }

    private fun List<CharacterInfo>.removeSecondFromField(step: Step, character: CharacterInfo?): List<CharacterInfo> = when (step) {
        is Step.FirstBan,
        is Step.SecondImmune,
        is Step.FirstPick,
        is Step.SecondBan,
        is Step.SecondPick -> if (character != null) this.minusElement(element = character) else this
        is Step.Ready,
        is Step.End,
        is Step.FirstImmune -> this
    }

    private fun List<CharacterSelectionInfo>.updateFirstBans(step: Step, character: CharacterInfo?): List<CharacterSelectionInfo> = when (step) {
        is Step.FirstBan -> {
            val position = this.indexOfFirst { info -> info.characterInfo == null }
            this.mapIndexed { index, characterSelectionInfo -> if (index == position) characterSelectionInfo.copy(characterInfo = character, isActive = false) else characterSelectionInfo }
        }
        is Step.FirstPick,
        is Step.SecondBan,
        is Step.SecondPick,
        is Step.FirstImmune,
        is Step.Ready,
        is Step.End,
        is Step.SecondImmune -> map { info -> info.copy(isActive = false) }
    }

    private fun List<CharacterSelectionInfo>.updateFirstPicks(step: Step, character: CharacterInfo?): List<CharacterSelectionInfo> = when (step) {
        is Step.FirstPick,
        is Step.FirstImmune -> {
            val position = this.indexOfFirst { info -> info.characterInfo == null }
            this.mapIndexed { index, characterSelectionInfo -> if (index == position) characterSelectionInfo.copy(characterInfo = character, isActive = false) else characterSelectionInfo }
        }
        is Step.FirstBan,
        is Step.SecondBan,
        is Step.SecondPick,
        is Step.Ready,
        is Step.End,
        is Step.SecondImmune -> map { info -> info.copy(isActive = false) }
    }

    private fun List<CharacterSelectionInfo>.updateFirstBansSelection(step: Step): List<CharacterSelectionInfo> = when (step) {
        is Step.FirstBan -> {
            val position = this.indexOfFirst { info -> info.characterInfo == null }
            this.mapIndexed { index, characterSelectionInfo -> if (index == position) characterSelectionInfo.copy(isActive = true) else characterSelectionInfo }
        }
        is Step.FirstPick,
        is Step.SecondBan,
        is Step.SecondPick,
        is Step.FirstImmune,
        is Step.Ready,
        is Step.End,
        is Step.SecondImmune -> map { info -> info.copy(isActive = false) }
    }

    private fun List<CharacterSelectionInfo>.updateFirstPicksSelection(step: Step): List<CharacterSelectionInfo> = when (step) {
        is Step.FirstPick,
        is Step.FirstImmune  -> {
            val position = this.indexOfFirst { info -> info.characterInfo == null }
            this.mapIndexed { index, characterSelectionInfo -> if (index == position) characterSelectionInfo.copy(isActive = true) else characterSelectionInfo }
        }
        is Step.FirstBan,
        is Step.SecondBan,
        is Step.SecondPick,
        is Step.Ready,
        is Step.End,
        is Step.SecondImmune -> map { info -> info.copy(isActive = false) }
    }

    private fun List<IndexedStep>.toFirstBans(): List<CharacterSelectionInfo> = filter { step -> step.step is Step.FirstBan }
        .map { CharacterSelectionInfo(index = it.index + 1) }

    private fun List<IndexedStep>.toFirstPicks(): List<CharacterSelectionInfo> = filter { step -> step.step is Step.FirstPick || step.step is Step.FirstImmune }
        .map { CharacterSelectionInfo(index = it.index + 1) }

    private fun List<CharacterSelectionInfo>.updateSecondBans(step: Step, character: CharacterInfo?): List<CharacterSelectionInfo> = when (step) {
        is Step.SecondBan -> {
            val position = this.indexOfFirst { info -> info.characterInfo == null }
            this.mapIndexed { index, characterSelectionInfo -> if (index == position) characterSelectionInfo.copy(characterInfo = character, isActive = false) else characterSelectionInfo }
        }
        is Step.FirstPick,
        is Step.FirstBan,
        is Step.SecondPick,
        is Step.FirstImmune,
        is Step.Ready,
        is Step.End,
        is Step.SecondImmune -> map { info -> info.copy(isActive = false) }
    }

    private fun List<CharacterSelectionInfo>.updateSecondPicks(step: Step, character: CharacterInfo?): List<CharacterSelectionInfo> = when (step) {
        is Step.SecondPick,
        is Step.SecondImmune -> {
            val position = this.indexOfFirst { info -> info.characterInfo == null }
            this.mapIndexed { index, characterSelectionInfo -> if (index == position) characterSelectionInfo.copy(characterInfo = character, isActive = false) else characterSelectionInfo }
        }
        is Step.FirstBan,
        is Step.SecondBan,
        is Step.FirstPick,
        is Step.Ready,
        is Step.End,
        is Step.FirstImmune -> map { info -> info.copy(isActive = false) }
    }

    private fun List<CharacterSelectionInfo>.updateSecondBansSelection(step: Step): List<CharacterSelectionInfo> = when (step) {
        is Step.SecondBan -> {
            val position = this.indexOfFirst { info -> info.characterInfo == null }
            this.mapIndexed { index, characterSelectionInfo -> if (index == position) characterSelectionInfo.copy(isActive = true) else characterSelectionInfo }
        }
        is Step.FirstPick,
        is Step.FirstBan,
        is Step.SecondPick,
        is Step.FirstImmune,
        is Step.Ready,
        is Step.End,
        is Step.SecondImmune -> map { info -> info.copy(isActive = false) }
    }

    private fun List<CharacterSelectionInfo>.updateSecondPicksSelection(step: Step): List<CharacterSelectionInfo> = when (step) {
        is Step.SecondPick,
        is Step.SecondImmune  -> {
            val position = this.indexOfFirst { info -> info.characterInfo == null }
            this.mapIndexed { index, characterSelectionInfo -> if (index == position) characterSelectionInfo.copy(isActive = true) else characterSelectionInfo }
        }
        is Step.FirstBan,
        is Step.SecondBan,
        is Step.FirstPick,
        is Step.Ready,
        is Step.End,
        is Step.FirstImmune -> map { info -> info.copy(isActive = false) }
    }

    private fun List<IndexedStep>.toSecondBans(): List<CharacterSelectionInfo> = filter { step -> step.step is Step.SecondBan }
        .map { CharacterSelectionInfo(index = it.index + 1) }

    private fun List<IndexedStep>.toSecondPicks(): List<CharacterSelectionInfo> = filter { step -> step.step is Step.SecondPick || step.step is Step.SecondImmune }
        .map { CharacterSelectionInfo(index = it.index + 1) }

    private fun List<Step>.toIndexed(): List<IndexedStep> = mapIndexed { index, step -> IndexedStep(index, step) }

    private data class IndexedStep(
        val index: Int,
        val step: Step
    )

}