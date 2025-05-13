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
    private var downloadId: Long = -1
    fun downloadAndInstallAPK(context: Context, url: String, fileName: String) {
        if (!context.packageManager.canRequestPackageInstalls()) {
            val intent = Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                ("package:" + context.packageName).toUri()
            )
            Toast.makeText(
                context,
                "Habilita el permiso para instalar aplicaciones desconocidas.",
                Toast.LENGTH_LONG
            ).show()
            context.startActivity(intent)
            return
        }

        Log.d("DownloadTest", "URI de descarga: ${url.toUri()}")
        val request = DownloadManager.Request(url.toUri())
            .setTitle("Descargando actualización...")
            .setDescription("Espere mientras se descarga la actualización.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true) // Permitir en datos móviles
            .setAllowedOverRoaming(true) // Bloquear en roaming

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        Log.d("DownloadTest", "Solicitud de descarga realizada. ID de descarga: $downloadId")
        val onCompleteReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    Log.d("DownloadTest", "Descarga completada. ID: $id")
                    val apkFile =
                        File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

                    if (apkFile.exists()) {
                        Log.d("DownloadTest", "El archivo APK existe en: ${apkFile.path}")

                        // Obtén el URI del archivo con FileProvider
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
        downloadId = downloadManager.enqueue(request)
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        ContextCompat.registerReceiver(
            context,
            onCompleteReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED // Configuración para evitar transmisiones externas
        )
        val intent = Intent()
        intent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, downloadId)
        onCompleteReceiver.onReceive(context, intent)
    }

}

private fun installAPK(context: Context, uri: Uri) {

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/vnd.android.package-archive")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Asegurarse de abrir la vista en una nueva tarea
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e("installAPK", "Error al iniciar la instalación del APK", e)
        Toast.makeText(context, "No se pudo iniciar la instalación", Toast.LENGTH_SHORT).show()
    }
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
