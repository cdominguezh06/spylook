package com.cogu.domain.repository

import com.cogu.domain.github.GitHubRelease
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {
    fun getLatestRelease(owner: String, repo: String): Flow<GitHubRelease>
}