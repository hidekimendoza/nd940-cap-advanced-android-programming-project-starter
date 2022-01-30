package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch
import java.util.*

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(val app:Application): AndroidViewModel(app) {
    // Create live data val for upcoming elections
    private val database = ElectionDatabase.getInstance(app)
    private val electionRepository = ElectionRepository(database)

    val elections: LiveData<List<ElectionDomainModel>> = electionRepository.elections
    val savedElections: LiveData<List<ElectionDomainModel>> = electionRepository.savedElections


    init {
        viewModelScope.launch {
            electionRepository.refreshElections()
        }
    }



    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}