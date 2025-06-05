package com.cogu.domain.github

interface FileDownloader {
    fun downloadAndInstallFile(
        url: String,
        fileName: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    )

}