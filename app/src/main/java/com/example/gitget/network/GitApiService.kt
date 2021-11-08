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

val logging : HttpLoggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

val httpClient : OkHttpClient.Builder = OkHttpClient.Builder()
    .addInterceptor(logging)


private const val BASE_URL = "https://api.github.com/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(httpClient.build())
    .build()

interface GitApiService {
    @GET("search/repositories")
    fun searchReposAsync(
        @Query("q") keyWord: String,
        @Query("page") pageNum: Int,
        @Query("per_page") sizePage: Int
    ): Deferred<Response<RepositoryInfoList>>

    @GET("repos/{owner}/{repo}/branches/master")
    fun getRepoDetailsAsync(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Deferred<Response<RepositoryDetails>>
}

object GitApi {
    val gitApiService: GitApiService by lazy { retrofit.create(GitApiService::class.java) }
}