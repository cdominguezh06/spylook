package com.cogu.spylook.model.github

import retrofit2.Call
import retrofit2.http.GET

interface GitHubAPI {
    @GET("repos/{owner}/{repo}/releases/latest")
    fun getLatestRelease(
        @retrofit2.http.Path("owner") owner: String,
        @retrofit2.http.Path("repo") repo: String
    ): Call<GitHubRelease>

}