package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {
    // Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUpcomingElections(vararg elections:Election)

    // Add select all election query
    @Query("SELECT * FROM election_table")
    fun observeUpcomingElections():LiveData<List<Election>>

    @Query("SELECT * FROM election_table")
    fun getUpcomingElections():List<Election>

    @Query("SELECT * FROM election_table WHERE saved = 1")
    fun observeSavedElections():LiveData<List<Election>>

    // Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getUpcomingElection(id: Int):Election

    // Add delete query
    @Query("DELETE FROM election_table")
    fun deleteAll()

    @Query("UPDATE election_table SET saved = 1 WHERE id = :id")
    fun markAsSaved(id: Int)

    @Query("UPDATE election_table SET saved = 0 WHERE id = :id")
    fun markAsNotSaved(id: Int)


}