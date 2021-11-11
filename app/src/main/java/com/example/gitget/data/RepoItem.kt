package com.example.gitget.data

data class RepoItem(
    val id: Int,
    val repoName: String,
    val repoUrl: String,
    val repoOwner: String,
    val lastCommitDate: String
)