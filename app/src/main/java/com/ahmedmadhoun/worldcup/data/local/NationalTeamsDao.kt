package com.ahmedmadhoun.worldcup.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ahmedmadhoun.worldcup.data.remote.pets_response.Pet
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationOb
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationResponse

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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopulation(populationOb: PopulationOb)

    @Query("SELECT * FROM pet")
    fun observePets(): LiveData<List<Pet>>

    @Query("SELECT * FROM population")
    fun observePopulation(): LiveData<List<PopulationOb>>

}