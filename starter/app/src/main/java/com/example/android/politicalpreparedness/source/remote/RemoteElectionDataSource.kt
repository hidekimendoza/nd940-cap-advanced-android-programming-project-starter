package com.example.android.politicalpreparedness.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.VoterInfo
import com.example.android.politicalpreparedness.network.models.asDomainModel
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.source.ElectionDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteElectionDataSource(
    private val apiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher
): ElectionDataSource {

    private val _upcomingElections: MutableLiveData<Result<List<ElectionDomainModel>>> = MutableLiveData()

    private val _electionDetails: MutableLiveData<Result<VoterInfo>> = MutableLiveData()

    suspend fun getRepresentativesFromLocation(address: Address): Result<List<Representative>>{
        return withContext(ioDispatcher) {
            val result: Result<List<Representative>> = try {
                val (offices, officials) = apiService.getRepresentatives(address.toFormattedString())
                val unifiedResponse = offices.flatMap { office -> office.getRepresentatives(officials) }
                Log.d("getRepresentativesFromLocation", "$unifiedResponse")
                Result.success(unifiedResponse)
            }
            catch (ex: java.lang.Exception){
                Result.failure(RuntimeException("Unable to get representatives due to: ${ex.message}"))
            }
            result
        }
    }

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

    suspend fun getElectionDetails(id:String, address: String): Result<VoterInfo>{
        return withContext(ioDispatcher){
            val result: Result<VoterInfo> = try {
                Log.d("getElectionDetails", "Getting election details from API")
                val election = apiService.getVoterInfo(id, address)
                Result.success(election.asDomainModel())
            }
            catch (ex: Exception){
                Result.failure(RuntimeException("Unable to get election details from network"))
            }
            Log.d("getElectionDetails", "Election details result: $result")
            _electionDetails.postValue(result)

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