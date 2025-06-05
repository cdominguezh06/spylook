package com.cogu.spylook.model.github

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.cogu.domain.github.FileDownloader

class AndroidFileDownloader(
    private val context: Context,
    private val launcher: ActivityResultLauncher<Intent>
) : FileDownloader{
    override fun downloadAndInstallFile(
        url: String,
        fileName: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            ApplicationUpdater.downloadAndInstallAPK(context, url, fileName, launcher)
            onSuccess()
        } catch (e: Exception) {
            onError(e)
        }

    }
}