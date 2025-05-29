package com.cogu.spylook.controller

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
import com.cogu.spylook.model.github.GitHubAPI
import com.cogu.spylook.model.github.GitHubRelease
import com.cogu.spylook.model.utils.ApplicationUpdater
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GithubController {

    companion object {
        private const val BASE_URL = "https://api.github.com/"
        private const val REPO_OWNER = "cdominguezh06"
        private const val REPO_NAME = "spylook"
        private const val CURRENT_VERSION = "0.3.2"

        private lateinit var INSTANCE: GithubController
        fun getInstance(): GithubController {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = GithubController()
            }
            return INSTANCE
        }
    }

    fun checkForUpdates(context: Context, unknownAppsPermissionLauncher: ActivityResultLauncher<Intent>) {
        val retrofit = getRetrofit();
        val gitHubApi = retrofit.create(GitHubAPI::class.java)
        Log.d("GithubController", "MANDO PETICION")
        gitHubApi.getLatestRelease(REPO_OWNER, REPO_NAME).enqueue(object : Callback<GitHubRelease> {
            override fun onResponse(call: Call<GitHubRelease>, response: Response<GitHubRelease>) {
                Log.d("GithubController", "ESTADO DE LA PETICION: ${response.isSuccessful} - ${response.code()}")
                if (response.isSuccessful) {
                    response.body()?.let { release ->
                        Log.d("GithubController", "ESTADO DE LA UPDATE:  ${isUpdateAvailable(release.tag_name, CURRENT_VERSION)}")
                        if (isUpdateAvailable(release.tag_name, CURRENT_VERSION)) {
                            displayUpdateDialog(context, release, unknownAppsPermissionLauncher)
                        }
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<GitHubRelease>, t: Throwable) {
                Log.e("GithubController", "Error fetching updates: ${t.message}")
            }
        })
    }

    private fun getRetrofit() : Retrofit{
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    private fun isUpdateAvailable(latestVersion: String, currentVersion: String): Boolean {
        val latestVersionParts = latestVersion.substring(2).split(".").map { it.toInt() }
        val currentVersionParts = currentVersion.split(".").map { it.toInt() }

        return latestVersionParts.zip(currentVersionParts)
            .any { (latest, current) -> latest > current }
    }

    private fun displayUpdateDialog(
        context: Context,
        release: GitHubRelease,
        unknownAppsPermissionLauncher: ActivityResultLauncher<Intent>
    ) {
        val markdownBody = release.body
        val styledMarkdown = formatMarkdownWithCustomTags(markdownBody)

        val builder = AlertDialog.Builder(context).setTitle("Nueva actualización disponible (${release.tag_name})")
            .setView(TextView(context).apply {
                textSize = 16f
                setPadding(32, 32, 32, 32)
                setText(styledMarkdown, TextView.BufferType.SPANNABLE)
            })

        val downloadUrl = release.assets.firstOrNull { it.name.endsWith(".apk") }?.browser_download_url!!
        builder.setPositiveButton("Descargar") { _, _ ->
                Log.d("GithubController", "Descargando desde URL: $downloadUrl")
                downloadFile(
                    context,
                    downloadUrl,
                    "spylook-${release.tag_name}.apk",
                    unknownAppsPermissionLauncher
                )
            }

        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun downloadFile(
        context: Context,
        url: String,
        fileName: String,
        unknownAppsPermissionLauncher: ActivityResultLauncher<Intent>
    ) {
        ApplicationUpdater.downloadAndInstallAPK(
            context,
            url,
            fileName,
            unknownAppsPermissionLauncher
        )
    }

    private fun formatMarkdownWithCustomTags(markdown: String): Spannable {
        val cleanMarkdown = markdown
            .replace(Regex("^>\\s*", RegexOption.MULTILINE), "") // Elimina "> " al inicio de líneas
            .replace(Regex("\\[!Warning\\]", RegexOption.IGNORE_CASE), "⚠️ Precaución")
            .replace(Regex("\\[!Note\\]", RegexOption.IGNORE_CASE), "💡 Notas")

        return SpannableStringBuilder(cleanMarkdown).apply {
            val warningIndex = cleanMarkdown.indexOf("⚠️")
            if (warningIndex != -1) {
                setSpan(
                    ForegroundColorSpan(Color.RED),
                    warningIndex,
                    warningIndex + 2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }
}