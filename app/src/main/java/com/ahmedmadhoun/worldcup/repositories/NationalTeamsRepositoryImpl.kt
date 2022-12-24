package com.ahmedmadhoun.worldcup.repositories

import androidx.lifecycle.LiveData
import com.ahmedmadhoun.worldcup.data.local.NationalTeam
import com.ahmedmadhoun.worldcup.data.local.NationalTeamsDao
import javax.inject.Inject

class NationalTeamsRepositoryImpl @Inject constructor(
    val nationalTeamsDao: NationalTeamsDao,
) : NationalTeamsRepository {

    override suspend fun insertNationalTeam(nationalTeam: NationalTeam) {
        nationalTeamsDao.insertNationalTeam(nationalTeam)
    }

    override suspend fun deleteNationalTeam(nationalTeam: NationalTeam) {
        nationalTeamsDao.deleteNationalTeam(nationalTeam)
    }

    override suspend fun deleteAllNationalTeams() {
        nationalTeamsDao.deleteAllNationalTeams()
    }


    override fun observeAllNationalTeams(listType: Int): LiveData<List<NationalTeam>> =
        nationalTeamsDao.observeAllNationalTeams(listType)

}