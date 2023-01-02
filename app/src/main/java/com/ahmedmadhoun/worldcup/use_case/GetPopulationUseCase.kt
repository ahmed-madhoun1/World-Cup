package com.ahmedmadhoun.worldcup.use_case

import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationOb
import com.ahmedmadhoun.worldcup.others.Status
import com.ahmedmadhoun.worldcup.repositories.population.PopulationRepository
import javax.inject.Inject

class GetPopulationUseCase @Inject constructor(
    private val populationRepository: PopulationRepository
) {
    suspend operator fun invoke(): List<PopulationOb> {
        val result = populationRepository.getPopulation()
        return when (result.status) {
            Status.SUCCESS -> {
                result.data?.data!!
            }
            Status.ERROR -> {
                emptyList<PopulationOb>()
            }
            else -> {
                emptyList<PopulationOb>()
            }
        }
    }
}