package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.VoterInfo
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.source.local.LocalElectionDataSource
import com.example.android.politicalpreparedness.source.remote.RemoteElectionDataSource
import com.udacity.project4.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(
    private val localElectionDataSource: LocalElectionDataSource,
    private val remoteElectionDataSource: RemoteElectionDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    val elections: LiveData<Result<List<ElectionDomainModel>>> =
        localElectionDataSource.observerFollowingElections()

    val savedElections: LiveData<Result<List<ElectionDomainModel>>> =
        localElectionDataSource.observerSavedElections()

    suspend fun refreshElectionsFromNetwork() {
        wrapEspressoIdlingResource {
            withContext(dispatcher) {
                val elections = remoteElectionDataSource.getElections()
                if (elections.isSuccess) {
                    // Remote elections
                    val electionList =
                        elections.getOrDefault(defaultValue = listOf<ElectionDomainModel>())
                    val localElections = localElectionDataSource.getElections()
                    // Clear datasource for passed elections
                    localElectionDataSource.deleteAll()
                    localElectionDataSource.saveElections(electionList)
                    val saved = localElections.getOrDefault(listOf()).filter { it.saved }
                    localElectionDataSource.saveElections(saved)
                    Log.i("refreshElectionsFromNetwork", "$electionList")
                }
            }
        }
    }

    suspend fun getElectionDetailsFromNetwork(id: String, date: String): Result<VoterInfo> {
        return wrapEspressoIdlingResource {
            withContext(dispatcher) {
                remoteElectionDataSource.getElectionDetails(id, date)
            }
        }
    }

    suspend fun getElectionById(electionId: Int): Result<ElectionDomainModel> {
        return wrapEspressoIdlingResource {
            withContext(dispatcher) {
                localElectionDataSource.getElection(electionId)
            }
        }
    }

    suspend fun followElectionState(election: ElectionDomainModel) {
        return wrapEspressoIdlingResource {
            withContext(dispatcher) {
                localElectionDataSource.markAsSaved(election)
            }
        }
    }

    suspend fun unfollowElectionState(election: ElectionDomainModel) {
        return wrapEspressoIdlingResource {
            withContext(dispatcher) {
                localElectionDataSource.markAsNotSaved(election)
            }
        }
    }

    suspend fun getRepresentatives(address: Address): Result<List<Representative>> {
        return wrapEspressoIdlingResource {
            withContext(dispatcher) {
                remoteElectionDataSource.getRepresentativesFromLocation(address)
            }
        }
    }
}