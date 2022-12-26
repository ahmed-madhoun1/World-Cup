package com.ahmedmadhoun.worldcup.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedmadhoun.worldcup.data.local.NationalTeam
import com.ahmedmadhoun.worldcup.others.Event
import com.ahmedmadhoun.worldcup.others.Resource
import com.ahmedmadhoun.worldcup.repositories.NationalTeamsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class NationalTeamsViewModel @Inject constructor(
    private val repository: NationalTeamsRepository
) : ViewModel() {

    var nationalTeams: LiveData<List<NationalTeam>> = MutableLiveData(null)

    private val _isRecyclerViewActive = MutableLiveData(true)
    val isRecyclerViewActive: LiveData<Boolean> = _isRecyclerViewActive

    fun setIsRecyclerViewActive(value: Boolean) {
        _isRecyclerViewActive.value = value
    }

    fun getData(listType: Int) {
        nationalTeams = repository.observeAllNationalTeams(listType = listType)
    }

    fun deleteAllNationalTeams(): Job {
        return viewModelScope.launch(Dispatchers.Default) {
            repository.deleteAllNationalTeams()
        }
    }

    fun deleteNationalTeam(nationalTeam: NationalTeam) = viewModelScope.launch {
        repository.deleteNationalTeam(nationalTeam)
    }

    fun insertNationalTeam(nationalTeam: NationalTeam) = viewModelScope.launch {
        repository.insertNationalTeam(nationalTeam.copy(id = Random.nextInt(2000, 5000)))
    }

}