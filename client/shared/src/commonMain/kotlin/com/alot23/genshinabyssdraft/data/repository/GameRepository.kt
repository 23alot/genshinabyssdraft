package com.alot23.genshinabyssdraft.data.repository

import com.alot23.genshinabyssdraft.entity.GameStep
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun isReady(): Boolean

    suspend fun observeGameSteps(gameToken: String, playerToken: String): Flow<List<GameStep>>

    fun onGameStep(gameStep: GameStep)

}