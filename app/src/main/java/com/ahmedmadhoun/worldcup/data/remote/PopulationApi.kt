package com.ahmedmadhoun.worldcup.data.remote

import com.ahmedmadhoun.worldcup.data.remote.pets_response.PetsResponse
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PopulationApi {

    @GET("data")
    suspend fun getPopulation(
        @Query("drilldowns") drilldowns: String = "Nation",
        @Query("measures") measures: String = "Population",
    ): Response<PopulationResponse>

}