package com.example.gitget.network

import com.example.gitget.network.models.RepositoryDetails
import com.example.gitget.network.models.RepositoryInfoList
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *   Remove Differed and don't use await() method and
 *   response.isSuccessful in GitRepository class
 */
interface GitApiService {
    @GET("search/repositories")
    fun searchReposAsync(
        @Query("q") keyWord: String
    ): Deferred<Response<RepositoryInfoList>>

    @GET("repos/{owner}/{repo}/branches/{branch_name}")
    fun getRepoDetailsAsync(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("branch_name") branchName: String
    ): Deferred<Response<RepositoryDetails>>
}
