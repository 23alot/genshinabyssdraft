package com.alot23.genshinabyssdraft.data.interactor

import com.alot23.genshinabyssdraft.entity.GameStep
import kotlinx.coroutines.flow.Flow

interface GameInteractor {

    fun observeGameSteps(gameToken: String, playerToken: String): Flow<GameStep>

    fun onGameStep(gameStep: GameStep)

}