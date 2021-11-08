package com.example.gitget.network.repository

import com.example.gitget.network.GitApi
import com.example.gitget.network.models.RepositoryDetails
import com.example.gitget.network.models.SimpleRepositoryInfo
import java.lang.Exception
import java.lang.NullPointerException

object GitRepImpl {

    suspend fun getDetails(
        info: SimpleRepositoryInfo?
    ) : RepositoryDetails? {
        info ?: throw NullPointerException()
        val response = GitApi.gitApiService.getRepoDetailsAsync(
            info.repositoryOwner.userName,
            info.repositoryName
        ).await()
        if (!response.isSuccessful)
            throw Exception(response.errorBody()?.string())
        return response.body()
    }

    suspend fun search(
        keyword: String?
    ): List<SimpleRepositoryInfo> {
        if (keyword.isNullOrBlank()) return emptyList()
        val response = GitApi.gitApiService.searchReposAsync(keyword).await()
        if (!response.isSuccessful)
            throw Exception(response.errorBody()?.string())
        return response.body()?.list ?: listOf()
    }
}