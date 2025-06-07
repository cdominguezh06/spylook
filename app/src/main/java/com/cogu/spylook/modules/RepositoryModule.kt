package com.cogu.spylook.modules

import com.cogu.data.github.GitHubAPI
import com.cogu.domain.repository.GitHubRepository
import com.cogu.spylook.github.AndroidGitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideGithubRepository(
        // dependencias de tu repo aquí, por ejemplo:
        githubApi: GitHubAPI
    ): GitHubRepository = AndroidGitHubRepository(githubApi)
}