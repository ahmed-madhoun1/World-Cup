package com.ahmedmadhoun.worldcup.di

import android.content.Context
import androidx.room.Room
import com.ahmedmadhoun.worldcup.data.local.NationalTeamsDao
import com.ahmedmadhoun.worldcup.data.local.WorldCupDatabase
import com.ahmedmadhoun.worldcup.repositories.NationalTeamsRepository
import com.ahmedmadhoun.worldcup.repositories.NationalTeamsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideWorldCupDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, WorldCupDatabase::class.java, "world_cup_database").build()

    @Provides
    @Singleton
    fun provideNationalTeamDao(
        database: WorldCupDatabase
    ) = database.nationalTeamsDao()

    @Provides
    @Singleton
    fun provideNationalTeamsRepository(
        dao: NationalTeamsDao,
    ) = NationalTeamsRepositoryImpl(dao) as NationalTeamsRepository


//    @Singleton
//    @Provides
//    fun provideGlideInstance(
//        @ApplicationContext context: Context
//    ) = Glide.with(context).setDefaultRequestOptions(
//        RequestOptions()
//            .placeholder(R.drawable.ic_image)
//            .error(R.drawable.ic_image)
//    )

}
