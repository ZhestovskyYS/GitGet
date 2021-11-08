package com.example.gitget.data

data class RepoItem(
    val id: Int,
    var repoName: String,
    var repoUrl: String,
    var repoOwner: String,
    var lastCommitDate: String
)