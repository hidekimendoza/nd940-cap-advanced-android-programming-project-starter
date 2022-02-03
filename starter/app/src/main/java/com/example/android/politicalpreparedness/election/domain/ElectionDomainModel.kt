package com.example.android.politicalpreparedness.election.domain

import android.os.Parcelable
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ElectionDomainModel(
    val id: Int,
    val name: String,
    val electionDay: Date,
    val division: Division,
    val saved: Boolean
) : Parcelable

fun List<ElectionDomainModel>.toDataModel(): List<Election> {
    return map {
        Election(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay,
            division = it.division,
            saved = it.saved
        )
    }
}
