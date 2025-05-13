package com.cogu.spylook.controller

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.cogu.spylook.model.github.GitHubAPI
import com.cogu.spylook.model.github.GitHubRelease
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.appcompat.app.AlertDialog
import io.noties.markwon.Markwon
import androidx.core.net.toUri
import com.cogu.spylook.BuildConfig
import com.cogu.spylook.model.utils.ApplicationUpdater
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.SpanFactory
import io.noties.markwon.core.MarkwonTheme

class GithubController {

    companion object {
        private lateinit var INSTANCE: GithubController
        fun getInstance(): GithubController {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = GithubController()
            }
            return INSTANCE
        }
    }

    fun checkForUpdates(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val gitHubApi = retrofit.create(GitHubAPI::class.java)
        val call = gitHubApi.getLatestRelease("cdominguezh06", "spylook")
        call.enqueue(object : Callback<GitHubRelease> {
            override fun onResponse(call: Call<GitHubRelease>, response: Response<GitHubRelease>) {
                if (response.isSuccessful) {
                    val release = response.body()
                    println(response.body())
                    release?.let {
                        val latestVersion = it.tag_name.substring(2)
                        println("aaaaa" +latestVersion)
                        val currentVersion = BuildConfig.VERSION_NAME
                        if (latestVersion > currentVersion) {
                            showUpdateDialog(context, it)
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Error al comprobar actualizaciones",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


            override fun onFailure(call: Call<GitHubRelease>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun showUpdateDialog(context: Context, release: GitHubRelease) {

        val markdownBody = release.body
        val styledMarkdown = processCustomTags(markdownBody)

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Nueva actualización disponible (${release.tag_name})")

        val textView = TextView(context)
        textView.textSize = 16f
        textView.setPadding(32, 32, 32, 32)

        textView.setText(styledMarkdown, TextView.BufferType.SPANNABLE)
        builder.setView(textView)

        val downloadUrl =
            release.assets.firstOrNull { it.name.endsWith(".apk") }?.browser_download_url
        downloadUrl?.let {
            builder.setPositiveButton("Descargar") { _, _ ->
                Log.d("DownloadTest", "URL de descarga: $downloadUrl")
                downloadFile(context, downloadUrl, "spylook-${release.tag_name}.apk")
            }
        }

        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun downloadFile(context: Context, url: String, fileName: String) {
        ApplicationUpdater.downloadAndInstallAPK(context, url, fileName)
    }


    private fun processCustomTags(markdown: String): Spannable {
        val cleanMarkdown = markdown
            .replace(
                Regex("^>\\s*", RegexOption.MULTILINE),
                ""
            ) // Elimina caracteres "> " al inicio de línea
            .replace(
                Regex("\\[!Warning\\]", RegexOption.IGNORE_CASE),
                "⚠️ Precaución"
            ) // Reemplaza [!Warning] por emoji o texto
            .replace(
                Regex("\\[!Note\\]", RegexOption.IGNORE_CASE),
                "💡 Notas"
            ) // Reemplaza [!Note] por emoji o texto

        val spannable = SpannableStringBuilder(cleanMarkdown)

        if (cleanMarkdown.contains("⚠️")) {
            val start = cleanMarkdown.indexOf("⚠️")
            val end = start + "⚠️".length
            spannable.setSpan(
                ForegroundColorSpan(Color.RED),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannable
    }


}