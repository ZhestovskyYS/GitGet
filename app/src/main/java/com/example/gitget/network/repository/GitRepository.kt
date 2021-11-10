package com.example.gitget.network.repository

import com.example.gitget.network.GitApiService
import com.example.gitget.network.models.RepositoryDetails
import com.example.gitget.network.models.SimpleRepositoryInfo
import java.lang.Exception
import java.lang.NullPointerException
import javax.inject.Inject

class GitRepository @Inject constructor(
    private val gitApiService: GitApiService
) {

    suspend fun getDetails(
        info: SimpleRepositoryInfo?
    ) : RepositoryDetails? {
        info ?: throw NullPointerException()
        var response = gitApiService.getRepoDetailsAsync(
            info.repositoryOwner.userName,
            info.repositoryName,
            "master"
        ).await()
        if (!response.isSuccessful){
            response = gitApiService.getRepoDetailsAsync(
                info.repositoryOwner.userName,
                info.repositoryName,
                "main"
            ).await()
            if (!response.isSuccessful)
                throw Exception(response.errorBody()?.string())
        }
        return response.body()
    }

    suspend fun search(
        keyword: String?
    ): List<SimpleRepositoryInfo> {
        if (keyword.isNullOrBlank()) return emptyList()
        val response = gitApiService.searchReposAsync(keyword).await()
        if (!response.isSuccessful)
            throw Exception(response.errorBody()?.string())
        return response.body()?.list ?: listOf()
    }
}