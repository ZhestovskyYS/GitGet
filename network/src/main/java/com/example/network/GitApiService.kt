package com.example.network

import com.example.network.models.RepositoryDetails
import com.example.network.models.RepositoryInfoList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitApiService {
    @GET("search/repositories")
    suspend fun searchReposAsync(
        @Query("q") keyWord: String
    ): Response<RepositoryInfoList>

    @GET("repos/{owner}/{repo}/branches/{branch_name}")
    suspend fun getRepoDetailsAsync(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("branch_name") branchName: String
    ): Response<RepositoryDetails>
}
