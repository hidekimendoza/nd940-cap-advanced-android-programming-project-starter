package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.utils.SingleLiveEvent
import kotlinx.coroutines.launch

// Construct ViewModel and provide election datasource
class ElectionsViewModel(val app: Application, private val electionRepository: ElectionRepository) :
    AndroidViewModel(app) {
    // Create live data val for upcoming elections
    val isLoadingDB: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val errorMessage: SingleLiveEvent<Int> = SingleLiveEvent()


    val upcomingElections: LiveData<List<ElectionDomainModel>> =
        Transformations.map(electionRepository.elections) {
            if (it.isSuccess) {
                it.getOrDefault(defaultValue = listOf())
            } else {
                errorMessage.value = R.string.error_get_election_list_network
                listOf()
            }
        }

    val savedElections = Transformations.map(electionRepository.savedElections) {
        if (it.isSuccess) {
            it.getOrDefault(defaultValue = listOf())
        } else {
            listOf()

        }
    }


    init {
        loadElections()
    }

    // Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    fun loadElections() {
        isLoadingDB.value = true
        viewModelScope.launch {
            Log.i(TAG, "Get elections from network")
            electionRepository.refreshElectionsFromNetwork()
        }
        isLoadingDB.value = false

    }

}
