package com.cogu.spylook.usecase

import com.cogu.domain.repository.GitHubRepository
import com.cogu.domain.usecase.github.CheckForUpdatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideCheckForUpdatesUseCase(
        repo: GitHubRepository
    ): CheckForUpdatesUseCase = CheckForUpdatesUseCase(repo)
}