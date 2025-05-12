package com.cogu.spylook.model.github

data class GitHubRelease(
    val tag_name : String,
    val body : String,
    val assets: List<ReleaseAsset>
)