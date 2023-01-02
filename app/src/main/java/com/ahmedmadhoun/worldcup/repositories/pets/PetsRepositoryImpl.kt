package com.ahmedmadhoun.worldcup.repositories.pets

import androidx.lifecycle.LiveData
import com.ahmedmadhoun.worldcup.data.local.NationalTeamsDao
import com.ahmedmadhoun.worldcup.data.remote.PetsApi
import com.ahmedmadhoun.worldcup.data.remote.pets_response.Pet
import com.ahmedmadhoun.worldcup.data.remote.pets_response.PetsResponse
import com.ahmedmadhoun.worldcup.others.Resource
import javax.inject.Inject

class PetsRepositoryImpl @Inject constructor(
    val api: PetsApi,
    val dao: NationalTeamsDao
) : PetsRepository {

    override suspend fun insertPet(pet: Pet) {
        dao.insertPet(pet)
    }

    override fun observePets(): LiveData<List<Pet>> =
        dao.observePets()

    override suspend fun getPets(): Resource<PetsResponse> {
        return try {
            val response = api.getPets()
            Resource.success(response.body())
        } catch (e: Exception) {
            Resource.error(e.message.toString(), null)
        }
    }

}