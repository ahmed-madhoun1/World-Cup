package com.ahmedmadhoun.worldcup.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NationalTeam::class],
    version = 1
)
abstract class WorldCupDatabase : RoomDatabase() {

    abstract fun nationalTeamsDao(): NationalTeamsDao

}