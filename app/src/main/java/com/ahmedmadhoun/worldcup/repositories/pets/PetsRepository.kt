package com.ahmedmadhoun.worldcup.repositories.pets

import androidx.lifecycle.LiveData
import com.ahmedmadhoun.worldcup.data.local.NationalTeam
import com.ahmedmadhoun.worldcup.data.remote.pets_response.Pet
import com.ahmedmadhoun.worldcup.data.remote.pets_response.PetsResponse
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationOb
import com.ahmedmadhoun.worldcup.others.Resource

interface PetsRepository {

    suspend fun insertPet(pet: Pet)

    fun observePets(): LiveData<List<Pet>>

    suspend fun getPets(): Resource<PetsResponse>

}