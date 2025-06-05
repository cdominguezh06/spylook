package com.cogu.domain.github

data class GitHubRelease(
    val tag_name : String,
    val body : String,
    val assets: List<ReleaseAsset>
)