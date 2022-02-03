package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.election.TAG
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.source.local.LocalElectionDataSource
import com.example.android.politicalpreparedness.source.remote.RemoteElectionDataSource
import com.udacity.project4.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(private val localElectionDataSource: LocalElectionDataSource,
                         private val remoteElectionDataSource: RemoteElectionDataSource,
                         private val dispatcher: CoroutineDispatcher = Dispatchers.IO){

    val elections: LiveData<Result<List<ElectionDomainModel>>> = localElectionDataSource.observerFollowingElections()

    val savedElections: LiveData<Result<List<ElectionDomainModel>>> = localElectionDataSource.observerSavedElections()

    suspend fun refreshElectionsFromNetwork(){
        wrapEspressoIdlingResource {
            withContext(dispatcher){
                val elections = remoteElectionDataSource.getElections()
                if(elections.isSuccess){
                    val electionList = elections.getOrDefault(defaultValue = listOf<ElectionDomainModel>())
                    Log.i("refreshElectionsFromNetwork", "$electionList")
                    localElectionDataSource.saveElections(electionList)
                }
            }
        }
    }
}