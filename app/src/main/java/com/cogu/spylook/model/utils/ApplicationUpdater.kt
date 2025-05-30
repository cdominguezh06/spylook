package com.cogu.spylook.model.utils

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri

object ApplicationUpdater {
    private const val UNKNOWN_APPS_PERMISSION_MSG =
        "Habilita el permiso para instalar aplicaciones desconocidas."

    private var downloadId: Long = -1

    fun downloadAndInstallAPK(
        context: Context,
        url: String,
        fileName: String,
        unknownAppsPermissionLauncher: ActivityResultLauncher<Intent>
    ) {
        if (!context.packageManager.canRequestPackageInstalls()) {
            handleUnknownAppSourcesPermission(context, unknownAppsPermissionLauncher)
            return
        }

        Log.d("GithubController", "URI de descarga: ${url.toUri()}")
        val request = DownloadManager.Request(url.toUri())
            .setTitle("Actualizacion de SpyLook")
            .setDescription("Espere mientras se descarga la actualización.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI or
                        DownloadManager.Request.NETWORK_MOBILE
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(request)
        Log.d("GithubController", "Solicitud de descarga realizada. ID de descarga: $downloadId")
    }

    private fun handleUnknownAppSourcesPermission(
        context: Context,
        unknownAppsPermissionLauncher: ActivityResultLauncher<Intent>
    ) {
        val intent = Intent(
            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            ("package:" + context.packageName).toUri()
        )
        Toast.makeText(context, UNKNOWN_APPS_PERMISSION_MSG, Toast.LENGTH_LONG).show()
        unknownAppsPermissionLauncher.launch(intent)

    }
}