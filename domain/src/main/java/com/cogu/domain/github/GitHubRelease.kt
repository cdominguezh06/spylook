package com.cogu.domain.github

data class GitHubRelease(
    val tagName : String,
    val body : String,
    val assets: List<ReleaseAsset>
)