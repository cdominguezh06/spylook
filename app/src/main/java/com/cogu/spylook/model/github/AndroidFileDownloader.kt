package com.cogu.spylook.model.github

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.cogu.domain.github.FileDownloader
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidFileDownloader @Inject constructor(
    @ApplicationContext private val context: Context,
) : FileDownloader{
    lateinit var launcher: ActivityResultLauncher<Intent>

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