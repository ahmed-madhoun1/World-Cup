package com.ahmedmadhoun.worldcup.di

import android.content.Context
import androidx.room.Room
import com.ahmedmadhoun.worldcup.data.local.NationalTeamsDao
import com.ahmedmadhoun.worldcup.data.local.WorldCupDatabase
import com.ahmedmadhoun.worldcup.data.remote.PetsApi
import com.ahmedmadhoun.worldcup.data.remote.PopulationApi
import com.ahmedmadhoun.worldcup.others.Constants
import com.ahmedmadhoun.worldcup.repositories.national_teams.NationalTeamsRepository
import com.ahmedmadhoun.worldcup.repositories.national_teams.NationalTeamsRepositoryImpl
import com.ahmedmadhoun.worldcup.repositories.pets.PetsRepository
import com.ahmedmadhoun.worldcup.repositories.pets.PetsRepositoryImpl
import com.ahmedmadhoun.worldcup.repositories.population.PopulationRepository
import com.ahmedmadhoun.worldcup.repositories.population.PopulationRepositoryImpl
import com.ahmedmadhoun.worldcup.use_case.GetPetsUseCase
import com.ahmedmadhoun.worldcup.use_case.GetPopulationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideWorldCupDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, WorldCupDatabase::class.java, "world_cup_database")
        .fallbackToDestructiveMigration().build()

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

    @Singleton
    @Provides
    fun providePetsRetrofit(): PetsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.PETS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PetsApi::class.java)
    }

    @Singleton
    @Provides
    fun providePopulationRetrofit(): PopulationApi {
        return Retrofit.Builder()
            .baseUrl(Constants.POPULATION_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PopulationApi::class.java)
    }


    @Provides
    @Singleton
    fun providePopulationRepository(
        api: PopulationApi,
        dao: NationalTeamsDao
    ) = PopulationRepositoryImpl(api, dao) as PopulationRepository

    @Provides
    @Singleton
    fun providePetsRepository(
        api: PetsApi,
        dao: NationalTeamsDao
    ) = PetsRepositoryImpl(api, dao) as PetsRepository

    fun providePetsUseCase(
        repository: PetsRepository
    ): GetPetsUseCase =
        GetPetsUseCase(repository)

    fun providePopulationUseCase(
        repository: PopulationRepository
    ): GetPopulationUseCase =
        GetPopulationUseCase(repository)

}
