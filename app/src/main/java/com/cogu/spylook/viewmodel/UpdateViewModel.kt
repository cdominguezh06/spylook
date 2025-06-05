package com.cogu.spylook.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cogu.domain.github.GitHubRelease
import com.cogu.domain.github.FileDownloader
import com.cogu.domain.usecase.github.CheckForUpdatesUseCase
import com.cogu.spylook.model.github.AndroidFileDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    application: Application,
    private val checkForUpdatesUseCase: CheckForUpdatesUseCase,
    private val fileDownloader: AndroidFileDownloader
) : AndroidViewModel(application) {

    companion object {
        private const val REPO_OWNER = "cdominguezh06"
        private const val REPO_NAME = "spylook"
        private const val CURRENT_VERSION = "1.5.0"
    }

    private val _releaseState = MutableStateFlow<GitHubRelease?>(null)
    val releaseState: StateFlow<GitHubRelease?> = _releaseState
        .onStart { checkForUpdates() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000), null)

    fun checkForUpdates() {
        viewModelScope.launch {
            checkForUpdatesUseCase(REPO_OWNER, REPO_NAME, CURRENT_VERSION)
                .catch { _releaseState.value = null }
                .collect { release ->
                    _releaseState.value = release
                }
        }
    }

    fun isUpdateAvailable(latestVersion: String, currentVersion: String = CURRENT_VERSION): Boolean {
        val latestVersionParts = latestVersion
            .replace("v.", "")
            .replace("v", "")
            .split(".").map { it.toIntOrNull() ?: 0 }
        val currentVersionParts = currentVersion.split(".").map { it.toIntOrNull() ?: 0 }

        val maxLength = maxOf(latestVersionParts.size, currentVersionParts.size)
        val adjustedLatest = latestVersionParts + List(maxLength - latestVersionParts.size) { 0 }
        val adjustedCurrent = currentVersionParts + List(maxLength - currentVersionParts.size) { 0 }

        for (i in adjustedLatest.indices) {
            if (adjustedLatest[i] > adjustedCurrent[i]) return true
            if (adjustedLatest[i] < adjustedCurrent[i]) return false
        }
        return false
    }


    fun downloadFile(url: String, fileName: String) {
        fileDownloader.downloadAndInstallFile(
            url,
            fileName,
            onSuccess = {},
            onError = {}
        )
    }

}