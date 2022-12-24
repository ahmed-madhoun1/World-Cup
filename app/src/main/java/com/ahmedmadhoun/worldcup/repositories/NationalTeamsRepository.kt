package com.ahmedmadhoun.worldcup.repositories

import androidx.lifecycle.LiveData
import com.ahmedmadhoun.worldcup.data.local.NationalTeam
import retrofit2.Response

interface NationalTeamsRepository {

    suspend fun insertNationalTeam(nationalTeam: NationalTeam)

    suspend fun deleteNationalTeam(nationalTeam: NationalTeam)

    suspend fun deleteAllNationalTeams()

    fun observeAllNationalTeams(listType: Int): LiveData<List<NationalTeam>>



}