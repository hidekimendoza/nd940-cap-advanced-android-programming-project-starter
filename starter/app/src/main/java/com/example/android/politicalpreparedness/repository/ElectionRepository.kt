package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(private val database: ElectionDatabase){

    val elections: LiveData<List<ElectionDomainModel>> = Transformations.map(database.electionDao.getUpcomingElections()){
        it.asDomainModel()
    }

    val savedElections: LiveData<List<ElectionDomainModel>> = Transformations.map(database.electionDao.getSavedElections()){
        it.asDomainModel()
    }

    suspend fun refreshElections(){
        withContext(Dispatchers.IO){
            val elections = CivicsApi.retrofitService.getElections()
            database.electionDao.insertUpcomingElections(*elections.elections.toTypedArray())
        }
    }
}