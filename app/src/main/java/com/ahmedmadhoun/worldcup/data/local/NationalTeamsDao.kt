package com.ahmedmadhoun.worldcup.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NationalTeamsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNationalTeam(nationalTeam: NationalTeam)

    @Delete
    suspend fun deleteNationalTeam(nationalTeam: NationalTeam)

    @Query("SELECT * FROM national_teams WHERE listType == :listType")
    fun observeAllNationalTeams(listType: Int): LiveData<List<NationalTeam>>


    @Query("SELECT * FROM national_teams ORDER BY `group`")
    fun observeAllNationalTeamsSortedByGroup(): LiveData<List<NationalTeam>>

    @Query("SELECT * FROM national_teams WHERE `group` == :new_group")
    fun observeNationalTeamsGroup(new_group: Int): LiveData<List<NationalTeam>>

    @Query("DELETE FROM national_teams")
    fun deleteAllNationalTeams()

}