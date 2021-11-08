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
        keyword: String?/*,
        from: Int,
        count: Int*/
    ): List<SimpleRepositoryInfo> {
        if (keyword.isNullOrBlank()) return emptyList()
        val response = GitApi.gitApiService.searchReposAsync(
            keyword,
            1/*getPageNumberByPosition(from, count)*/,
            10/*count*/
        ).await()
        if (!response.isSuccessful)
            throw Exception(response.errorBody()?.string())
        return response.body()?.list ?: listOf()
    }

   /* private fun getPageNumberByPosition(from: Int, count: Int): Int {
        if (from == 0) return 1
        if (count == 0) throw IllegalArgumentException("page size must not be null")
        if (from < count) return 1
        return from / count + 1
    }*/
}