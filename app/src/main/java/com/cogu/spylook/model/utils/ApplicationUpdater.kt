package com.cogu.spylook.model.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import java.io.File

object ApplicationUpdater {
    fun downloadAndInstallAPK(context: Context, url: String, fileName: String) {

        val request = DownloadManager.Request(url.toUri())
            .setTitle("Descargando actualización...")
            .setDescription("Espere mientras se descarga la actualización.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true) // Permitir en datos móviles
            .setAllowedOverRoaming(true) // Bloquear en roaming

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        // Registrar un broadcast para la descarga (cuando se complete)
        val onCompleteReceiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    // Posteriormente al completar la descarga, instalar y eliminar el APK
                    val apkFile = downloadManager.getUriForDownloadedFile(downloadId)

                    if (apkFile != null) {
                        installAPK(context, apkFile)
                    } else {
                        Toast.makeText(
                            context,
                            "Error al descargar el archivo APK.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // Desregistrar receptor
                    context.unregisterReceiver(this)
                }
            }
        }
        ContextCompat.registerReceiver(
            context,
            onCompleteReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

}

private fun installAPK(context: Context, uri: Uri) {
    // Verifica si el permiso de instalación de orígenes desconocidos está habilitado
    if (!context.packageManager.canRequestPackageInstalls()) {
        val intent = Intent(
            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            ("package:" + context.packageName).toUri()
        )
        context.startActivity(intent)
        return
    }

    // Crear un intent para instalar el APK
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/vnd.android.package-archive")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(intent)
    deleteAPKAfter(context, uri)
}


private fun deleteAPKAfter(context: Context, file: Uri) {
    try {
        val filePath = File(file.path ?: return)
        if (filePath.exists()) {
            filePath.delete()
        } else {
            Log.w("deleteAPKAfter", "File does not exist: $file")
        }
    } catch (e: Exception) {
        Log.e("deleteAPKAfter", "Error deleting file: $file", e)
    }
}
