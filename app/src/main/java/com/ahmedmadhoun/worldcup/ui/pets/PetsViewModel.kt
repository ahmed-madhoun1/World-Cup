package com.ahmedmadhoun.worldcup.ui.pets

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedmadhoun.worldcup.data.remote.pets_response.Pet
import com.ahmedmadhoun.worldcup.repositories.pets.PetsRepository
import com.ahmedmadhoun.worldcup.use_case.GetPetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetsViewModel @Inject constructor(
    val getPetsUseCase: GetPetsUseCase,
    val repository: PetsRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            getPets()
        }
    }

    val getPetsStateFlow: MutableStateFlow<List<Pet>> = MutableStateFlow(emptyList())

    private fun getPets() {
        viewModelScope.launch {
            Log.e("TAG", "onViewCreated: ${getPetsUseCase()}")
            getPetsStateFlow.emit(getPetsUseCase())
        }
    }

     fun insetPetsIntoDatabase(pets: List<Pet>) {
        viewModelScope.launch {
            if (pets.isNotEmpty()) {
                pets.forEach {
                    repository.insertPet(it)
                }
            }
        }
    }

}