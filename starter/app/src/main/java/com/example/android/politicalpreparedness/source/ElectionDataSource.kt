package com.example.android.politicalpreparedness.source

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel

interface ElectionDataSource {
    suspend fun getElections(): Result<List<ElectionDomainModel>>
    fun observerFollowingElections(): LiveData<Result<List<ElectionDomainModel>>>

    suspend fun saveElections(elections: List<ElectionDomainModel>)

    suspend fun markAsSaved(election: ElectionDomainModel)

    suspend fun markAsNotSaved(election: ElectionDomainModel)
    suspend fun deleteAll()
}