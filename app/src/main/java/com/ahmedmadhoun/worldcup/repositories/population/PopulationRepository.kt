package com.ahmedmadhoun.worldcup.repositories.population

import androidx.lifecycle.LiveData
import com.ahmedmadhoun.worldcup.data.local.NationalTeam
import com.ahmedmadhoun.worldcup.data.remote.pets_response.Pet
import com.ahmedmadhoun.worldcup.data.remote.pets_response.PetsResponse
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationOb
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationResponse
import com.ahmedmadhoun.worldcup.others.Resource

interface PopulationRepository {

    suspend fun insertPopulation(populationOb: PopulationOb)

    fun observePopulation(): LiveData<List<PopulationOb>>

    suspend fun getPopulation(): Resource<PopulationResponse>

}