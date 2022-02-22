package com.example.android.politicalpreparedness.network.models

import androidx.room.*
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.squareup.moshi.*
import java.util.*

@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division,
        @ColumnInfo(name = "saved")val saved: Boolean = false // Add saved boolean for saved election feature
)

fun List<Election>.asDomainModel(): List<ElectionDomainModel> {
        return map{
                ElectionDomainModel(
                        id = it.id,
                        name = it.name,
                        electionDay = it.electionDay,
                        division = it.division,
                        saved = it.saved
                )
        }
}

fun Election.asDomainModel(): ElectionDomainModel {
        return let{
                ElectionDomainModel(
                        id = it.id,
                        name = it.name,
                        electionDay = it.electionDay,
                        division = it.division,
                        saved = it.saved
                )
        }
}