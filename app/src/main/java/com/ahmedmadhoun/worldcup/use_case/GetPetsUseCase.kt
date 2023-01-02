package com.ahmedmadhoun.worldcup.use_case

import com.ahmedmadhoun.worldcup.data.remote.pets_response.Pet
import com.ahmedmadhoun.worldcup.others.Status
import com.ahmedmadhoun.worldcup.repositories.pets.PetsRepository
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(
    private val petsRepository: PetsRepository
) {
    suspend operator fun invoke(): List<Pet>{
        val result = petsRepository.getPets()
        return when (result.status) {
            Status.SUCCESS -> {
                result.data?.entries!!
            }
            Status.ERROR -> {
                emptyList()
            }
            else -> {
                emptyList()
            }
        }
    }
}