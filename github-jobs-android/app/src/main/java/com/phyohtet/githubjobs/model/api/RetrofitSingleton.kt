package com.phyohtet.githubjobs.model.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitSingleton {

    private const val BASE_URL = "https://jobs.github.com/"

    private val retrofit: Retrofit
    private val client: OkHttpClient

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
    }

    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    fun cancelAllRequest() {
        client.dispatcher().cancelAll()
    }

}