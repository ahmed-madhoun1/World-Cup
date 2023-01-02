package com.ahmedmadhoun.worldcup.ui.population

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationOb
import com.ahmedmadhoun.worldcup.repositories.population.PopulationRepository
import com.ahmedmadhoun.worldcup.use_case.GetPopulationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopulationViewModel @Inject constructor(
    val getPopulationUseCase: GetPopulationUseCase,
    val repository: PopulationRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            getPopulation()
        }
    }

    val getPopulationStateFlow: MutableStateFlow<List<PopulationOb>> = MutableStateFlow(emptyList())

    private fun getPopulation() {
        viewModelScope.launch {
            Log.e("TAG", "onViewCreated: ${getPopulationUseCase()}")
            getPopulationStateFlow.emit(getPopulationUseCase())
        }
    }

     fun insetPopulationIntoDatabase(population: List<PopulationOb>) {
        viewModelScope.launch {
            if (population.isNotEmpty()) {
                population.forEach {
                    repository.insertPopulation(it)
                }
            }
        }
    }

}