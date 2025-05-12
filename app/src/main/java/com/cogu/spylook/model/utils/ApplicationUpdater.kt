package com.cogu.spylook.model.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
            .setAllowedOverRoaming(false) // Bloquear en roaming

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        // Registrar un broadcast para la descarga (cuando se complete)
        val onCompleteReceiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    // Posteriormente al completar la descarga, instalar y eliminar el APK
                    val apkFile = File(
                        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                        fileName
                    )
                    if (apkFile.exists()) {
                        installAPK(context, apkFile)
                    } else {
                        Toast.makeText(context, "Error al descargar el archivo APK.", Toast.LENGTH_SHORT).show()
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

    private fun installAPK(context: Context, file: File) {
        // Crear el URI utilizando FileProvider
        val apkUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val canInstall = context.packageManager.canRequestPackageInstalls()
        if (!canInstall) {
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                data = "package:${context.packageName}".toUri()
            }
            context.startActivity(intent)
        }

        // Configurar la eliminación del archivo APK después de cierto tiempo
        deleteAPKAfter(context, file)
    }

    private fun deleteAPKAfter(context: Context, file: File) {
        // Opcionalmente podrás eliminar el archivo con un retraso para dar tiempo a la instalación
        android.os.Handler(context.mainLooper).postDelayed({
            if (file.exists()) {
                val isDeleted = file.delete()
                if (isDeleted) {
                    Toast.makeText(context, "Archivo APK eliminado.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No se pudo eliminar el archivo APK.", Toast.LENGTH_SHORT).show()
                }
            }
        }, 10000) // 10 segundos de retraso
    }

}