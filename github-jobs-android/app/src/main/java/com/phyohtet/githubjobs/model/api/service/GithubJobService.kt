package com.phyohtet.githubjobs.model.api.service

import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubJobService {

    @GET("positions.json")
    fun findPositions(
            @Query("description")
            description: String,
            @Query("location")
            location: String,
            @Query("page")
            page: Int
    ): Call<List<JobPositionDTO>>

    @GET("positions.json")
    fun findPositions(
            @Query("description")
            description: String,
            @Query("location")
            location: String,
            @Query("full_time")
            fullTime: Boolean,
            @Query("page")
            page: Int
    ): Call<List<JobPositionDTO>>

    @GET("positions/{id}")
    fun getPosition(@Path("id") id: String): Call<JobPositionDTO>

}