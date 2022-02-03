package com.example.android.politicalpreparedness.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.election.domain.toDataModel
import com.example.android.politicalpreparedness.network.models.asDomainModel
import com.example.android.politicalpreparedness.source.ElectionDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class LocalElectionDataSource(
    private val database: ElectionDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : ElectionDataSource {

    override fun observerFollowingElections(): LiveData<Result<List<ElectionDomainModel>>> {
        return database.electionDao.observeUpcomingElections().map{
            Result.success(it.asDomainModel())
        }
    }

    fun observerSavedElections(): LiveData<Result<List<ElectionDomainModel>>> {
        return database.electionDao.observeSavedElections().map{
            Result.success(it.asDomainModel())
        }
    }

    override suspend fun getElections(): Result<List<ElectionDomainModel>> {
        return withContext(ioDispatcher){
            val result: Result<List<ElectionDomainModel>> = try {
                Result.success(database.electionDao.getUpcomingElections().asDomainModel())
            } catch(exc: Exception){
                Result.failure(
                    RuntimeException("Unable to get election from database"))
            }
            result
        }
    }

    override suspend fun saveElections(elections: List<ElectionDomainModel>) = withContext(ioDispatcher) {
        database.electionDao.insertUpcomingElections(*elections.toDataModel().toTypedArray())}

    override suspend fun markAsSaved(election: ElectionDomainModel) = withContext(ioDispatcher) {
        database.electionDao.markAsSaved(election.id)
    }

    override suspend fun markAsNotSaved(election: ElectionDomainModel) = withContext(ioDispatcher)  {
        database.electionDao.markAsNotSaved(election.id)

    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        database.electionDao.deleteAll()
    }
}