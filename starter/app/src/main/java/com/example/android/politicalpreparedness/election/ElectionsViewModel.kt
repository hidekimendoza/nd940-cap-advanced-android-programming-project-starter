package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch

// Construct ViewModel and provide election datasource
class ElectionsViewModel(val app:Application, private val electionRepository: ElectionRepository): AndroidViewModel(app) {
    // Create live data val for upcoming elections
    private val _isLoadingDB = MutableLiveData(false)
    val isLoadingDB: LiveData<Boolean>
        get() = _isLoadingDB


//    private val _navigateToElectionDetail = MutableLiveData<ElectionDomainModel?>()
//    val navigateToElectionDetail
//        get() = _navigateToElectionDetail
//
//    fun onElectionClicked(selectedElection: ElectionDomainModel){
//        _navigateToElectionDetail.value = selectedElection
//    }
//
//    fun onElectionDetailsNavigated() {
//        _navigateToElectionDetail.value = null
//    }


    val upcomingElections: LiveData<List<ElectionDomainModel>> = Transformations.map(electionRepository.elections){ it ->
        if(it.isSuccess){
            it.getOrDefault(defaultValue = listOf<ElectionDomainModel>())
        }
        else{
            listOf<ElectionDomainModel>()
        }
    }


    val savedElections = Transformations.map(electionRepository.savedElections) { it ->
        if (it.isSuccess) {
            it.getOrDefault(defaultValue = listOf<ElectionDomainModel>())
        } else {
            listOf<ElectionDomainModel>()
        }
    }


    init {
        loadElections()
    }

    fun loadElections(){
        _isLoadingDB.value = true
        viewModelScope.launch {
            Log.i(TAG, "Get elections from network")
            electionRepository.refreshElectionsFromNetwork()
        }
        _isLoadingDB.value = false

    }



    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}
