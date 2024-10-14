package com.alot23.genshinabyssdraft.data.repository

import com.alot23.genshinabyssdraft.entity.GameStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocalGameRepository : GameRepository {
    private val steps: MutableStateFlow<List<GameStep>> = MutableStateFlow(emptyList())

    override fun isReady(): Boolean = true

    override suspend fun observeGameSteps(gameToken: String, playerToken: String): Flow<List<GameStep>> = steps.asStateFlow()

    override fun onGameStep(gameStep: GameStep) {
        steps.update { previousList -> previousList.plus(element = gameStep) }
    }
}