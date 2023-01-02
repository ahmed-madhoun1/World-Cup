package com.ahmedmadhoun.worldcup.repositories.population

import androidx.lifecycle.LiveData
import com.ahmedmadhoun.worldcup.data.local.NationalTeamsDao
import com.ahmedmadhoun.worldcup.data.remote.PopulationApi
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationOb
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationResponse
import com.ahmedmadhoun.worldcup.others.Resource
import javax.inject.Inject

class PopulationRepositoryImpl @Inject constructor(
    val api: PopulationApi,
    val dao: NationalTeamsDao
) : PopulationRepository {
    override suspend fun insertPopulation(populationOb: PopulationOb) {
        dao.insertPopulation(populationOb)
    }

    override fun observePopulation(): LiveData<List<PopulationOb>> =
        dao.observePopulation()

    override suspend fun getPopulation(): Resource<PopulationResponse> {
        return try {
            val response = api.getPopulation()
            Resource.success(response.body())
        } catch (e: Exception) {
            Resource.error(e.message.toString(), null)
        }
    }

}