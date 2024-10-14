package com.alot23.genshinabyssdraft.di

import com.alot23.genshinabyssdraft.data.interactor.GameInteractor
import com.alot23.genshinabyssdraft.data.interactor.GameInteractorImpl
import com.alot23.genshinabyssdraft.data.repository.LocalGameRepository
import com.alot23.genshinabyssdraft.data.repository.RemoteGameRepository
import org.koin.dsl.module

val gameModule = module {
    single<GameInteractor> {
        GameInteractorImpl(
            localRepository = LocalGameRepository(),
            remoteRepository = RemoteGameRepository()
        )
    }
}