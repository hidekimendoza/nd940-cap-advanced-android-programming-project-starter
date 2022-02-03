package com.example.android.politicalpreparedness.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.asDomainModel
import com.example.android.politicalpreparedness.source.ElectionDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class RemoteElectionDataSource(
    private val apiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher
): ElectionDataSource {

    private val _upcomingElections: MutableLiveData<Result<List<ElectionDomainModel>>> = MutableLiveData()

    override suspend fun getElections(): Result<List<ElectionDomainModel>> {
        return withContext(ioDispatcher){
            val result: Result<List<ElectionDomainModel>> = try {
                val elections = apiService.getElections()
                Result.success(elections.elections.asDomainModel())
            }
            catch (ex: Exception){
                Result.failure(RuntimeException("Unable to get elections from network"))
            }
            _upcomingElections.postValue(result)
            result
        }
    }

    override fun observerFollowingElections(): LiveData<Result<List<ElectionDomainModel>>> {
        return _upcomingElections
    }

    override suspend fun saveElections(elections: List<ElectionDomainModel>) {
        // Not required
    }

    override suspend fun markAsSaved(election: ElectionDomainModel) {
        // Not required

    }

    override suspend fun markAsNotSaved(election: ElectionDomainModel) {
        // Not required
    }

    override suspend fun deleteAll() {
        // Not required
    }
}