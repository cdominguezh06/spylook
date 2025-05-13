package com.cogu.spylook.model.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File

object ApplicationUpdater {
    private const val APK_MIME_TYPE = "application/vnd.android.package-archive"
    private const val UNKNOWN_APPS_PERMISSION_MSG =
        "Habilita el permiso para instalar aplicaciones desconocidas."
    private const val DOWNLOAD_COMPLETE_LOG = "Descarga completada. ID: "

    private var downloadId: Long = -1

    fun downloadAndInstallAPK(context: Context, url: String, fileName: String) {
        if (!context.packageManager.canRequestPackageInstalls()) {
            handleUnknownAppSourcesPermission(context)
            return
        }

        Log.d("DownloadTest", "URI de descarga: ${url.toUri()}")
        val request = DownloadManager.Request(url.toUri())
            .setTitle("Descargando actualización...")
            .setDescription("Espere mientras se descarga la actualización.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(request)
        registerDownloadReceiver(context, fileName)
        Log.d("DownloadTest", "Solicitud de descarga realizada. ID de descarga: $downloadId")
    }

    private fun handleUnknownAppSourcesPermission(context: Context) {
        val intent = Intent(
            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            ("package:" + context.packageName).toUri()
        )
        Toast.makeText(context, UNKNOWN_APPS_PERMISSION_MSG, Toast.LENGTH_LONG).show()
        context.startActivity(intent)
    }

    private fun registerDownloadReceiver(context: Context, fileName: String) {
        val onCompleteReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    Log.d("DownloadTest", "$DOWNLOAD_COMPLETE_LOG$id")

                    val apkFile =
                        File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
                    if (apkFile.exists()) {
                        val apkUri: Uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            apkFile
                        )
                        installAPK(context, apkUri)
                    } else {
                        Log.e("DownloadTest", "El archivo descargado no se encontró.")
                    }
                } else {
                    Log.d("DownloadTest", "El ID de la descarga completada no coincide. Ignorando.")
                }
            }
        }

        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        ContextCompat.registerReceiver(
            context,
            onCompleteReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    private fun installAPK(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, APK_MIME_TYPE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("installAPK", "Error al iniciar la instalación del APK", e)
            Toast.makeText(context, "No se pudo iniciar la instalación", Toast.LENGTH_SHORT).show()
        }
    }
}