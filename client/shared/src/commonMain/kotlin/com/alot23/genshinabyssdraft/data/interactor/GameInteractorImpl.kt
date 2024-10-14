package com.alot23.genshinabyssdraft.data.interactor

import com.alot23.genshinabyssdraft.data.repository.GameRepository
import com.alot23.genshinabyssdraft.entity.GameStep
import kotlinx.coroutines.flow.Flow

class GameInteractorImpl(
    private val localRepository: GameRepository,
    private val remoteRepository: GameRepository
): GameInteractor {

    override fun observeGameSteps(gameToken: String, playerToken: String): Flow<GameStep> = when (remoteRepository.isReady()) {
        true -> remoteRepository.observeGameSteps(gameToken = gameToken, playerToken = playerToken)
        false -> localRepository.observeGameSteps(gameToken = gameToken, playerToken = playerToken)
    }

    override fun onGameStep(gameStep: GameStep) = when (remoteRepository.isReady()){
        true -> remoteRepository.onGameStep(gameStep = gameStep)
        false -> localRepository.onGameStep(gameStep = gameStep)
    }

}