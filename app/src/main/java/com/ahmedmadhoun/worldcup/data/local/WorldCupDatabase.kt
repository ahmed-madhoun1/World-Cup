package com.ahmedmadhoun.worldcup.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmedmadhoun.worldcup.data.remote.pets_response.Pet
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationOb

@Database(
    entities = [NationalTeam::class, Pet::class, PopulationOb::class],
    version = 2
)
abstract class WorldCupDatabase : RoomDatabase() {

    abstract fun nationalTeamsDao(): NationalTeamsDao

}