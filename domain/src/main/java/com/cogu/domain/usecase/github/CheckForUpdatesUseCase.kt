package com.cogu.domain.usecase.github

import com.cogu.domain.github.GitHubRelease
import com.cogu.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CheckForUpdatesUseCase(private val repo : GitHubRepository) {

    operator fun invoke(owner: String, repoName: String, currentVersion: String): Flow<GitHubRelease?> {
        return repo.getLatestRelease(owner, repoName).map { release ->
            if (isUpdateAvailable(release.tag_name, currentVersion)) release else null
        }
    }

    private fun isUpdateAvailable(latestVersion: String, currentVersion: String): Boolean {
        val latestVersionParts = latestVersion
            .replace("v.", "")
            .replace("v", "")
            .split(".").map { it.toInt() }
        val currentVersionParts = currentVersion.split(".").map { it.toInt() }

        val maxLength = maxOf(latestVersionParts.size, currentVersionParts.size)
        val adjustedLatest = latestVersionParts + List(maxLength - latestVersionParts.size) { 0 }
        val adjustedCurrent = currentVersionParts + List(maxLength - currentVersionParts.size) { 0 }

        for (i in adjustedLatest.indices) {
            if (adjustedLatest[i] > adjustedCurrent[i]) return true
            if (adjustedLatest[i] < adjustedCurrent[i]) return false
        }

        return false

    }
}