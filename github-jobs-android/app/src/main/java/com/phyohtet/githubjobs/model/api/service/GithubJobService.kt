package com.phyohtet.githubjobs.model.api.service

import com.phyohtet.githubjobs.model.dto.PositionDTO
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
            location: String
    ): Call<List<PositionDTO>>

    @GET("positions.json")
    fun findPositions(
            @Query("description")
            description: String,
            @Query("location")
            location: String,
            @Query("fulltime")
            fullTime: Boolean
    ): Call<List<PositionDTO>>

    @GET("positions/{id}")
    fun getPosition(@Path("id") id: String): Call<PositionDTO>

}