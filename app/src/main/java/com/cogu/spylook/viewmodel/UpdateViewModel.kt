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
import com.cogu.domain.github.DownloadData
import com.cogu.domain.github.GitHubRelease
import com.cogu.domain.github.FileDownloader
import com.cogu.domain.usecase.github.CheckForUpdatesUseCase
import com.cogu.spylook.model.github.AndroidFileDownloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    application: Application,
    private val checkForUpdatesUseCase: CheckForUpdatesUseCase,
) : AndroidViewModel(application) {

    companion object {
        private const val REPO_OWNER = "cdominguezh06"
        private const val REPO_NAME = "spylook"
        private const val CURRENT_VERSION = "1.3.0"
    }

    private val _downloadEvent = MutableSharedFlow<DownloadData>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val downloadEvent: SharedFlow<DownloadData> = _downloadEvent

    fun onDownloadClicked(url: String, filename: String) {
        Log.d("UpdateViewModel", "onDownloadClicked llamado: $url")
        viewModelScope.launch {
            _downloadEvent.emit(DownloadData(url, filename))
        }
    }

    private val _installPermissionGranted = MutableSharedFlow<Unit>()
    val installPermissionGranted: SharedFlow<Unit> = _installPermissionGranted

    fun onInstallPermissionResult(granted: Boolean) {
        viewModelScope.launch {
            if (granted) {
                _installPermissionGranted.emit(Unit)
            }
        }
    }

    private val _releaseState = MutableStateFlow<GitHubRelease?>(null)
    val releaseState: StateFlow<GitHubRelease?> = _releaseState
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun checkForUpdates() {
        viewModelScope.launch {
            checkForUpdatesUseCase(REPO_OWNER, REPO_NAME, CURRENT_VERSION)
                .catch { e -> _releaseState.emit(null); throw e }
                .collect { release ->
                    _releaseState.emit(release)
                }
        }
    }
}