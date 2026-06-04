package com.narrative.app.di

import com.narrative.app.data.repository.CardRepositoryImpl
import com.narrative.app.data.repository.ProjectRepositoryImpl
import com.narrative.app.domain.repository.CardRepository
import com.narrative.app.domain.repository.ProjectRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindCardRepository(impl: CardRepositoryImpl): CardRepository

    @Binds @Singleton
    abstract fun bindProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository
}
