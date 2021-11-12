package com.example.network.repository

import com.example.network.GitApiService
import com.example.network.models.RepositoryDetails
import com.example.network.models.SimpleRepositoryInfo
import java.lang.Exception
import javax.inject.Inject

class GitRepository @Inject constructor(
    private val gitApiService: GitApiService
) {

    suspend fun getDetails(repoName: String, repoOwner: String):
            RepositoryDetails? {
        var response = gitApiService.getRepoDetailsAsync(repoOwner, repoName, "master")
        if (!response.isSuccessful) {
            response = gitApiService
                .getRepoDetailsAsync(repoOwner, repoName, "main")
            if (!response.isSuccessful)
                throw Exception(response.errorBody()?.string())
        }
        return response.body()
    }

    suspend fun search(keyword: String?):
            List<SimpleRepositoryInfo> {
        if (keyword.isNullOrBlank()) return emptyList()
        val response = gitApiService.searchReposAsync(keyword)
        if (!response.isSuccessful)
            throw Exception(response.errorBody()?.string())
        return response.body()?.list ?: listOf()
    }
}