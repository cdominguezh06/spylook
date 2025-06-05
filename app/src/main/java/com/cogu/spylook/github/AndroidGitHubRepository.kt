package com.cogu.spylook.github

import com.cogu.data.github.GitHubAPI
import com.cogu.domain.github.GitHubRelease
import com.cogu.domain.repository.GitHubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AndroidGitHubRepository @Inject constructor(
    private val gitHubAPI: GitHubAPI
) : GitHubRepository {
    override fun getLatestRelease(owner: String, repo: String): Flow<GitHubRelease> = flow {
        val response = gitHubAPI.getLatestRelease(owner, repo).execute()
        if (response.isSuccessful) {
            val release = response.body()
            if (release != null) {
                emit(release)
            } else {
                throw Exception("No hay releases encontrados")
            }
        } else {
            throw Exception("Error al recuperar el release: ${response.code()}")
        }
    }.flowOn(Dispatchers.IO)

}