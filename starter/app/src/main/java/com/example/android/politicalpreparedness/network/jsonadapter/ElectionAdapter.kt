package com.example.android.politicalpreparedness.network.jsonadapter

import com.example.android.politicalpreparedness.network.models.Division
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ElectionAdapter {
    @FromJson
    fun divisionFromJson (ocdDivisionId: String): Division {
        val countryDelimiter = "country:"
        val stateDelimiter = "state:"
        val districtDelimiter = "district:"
        val country = ocdDivisionId.substringAfter(countryDelimiter,"")
                .substringBefore("/")
        var state = ocdDivisionId.substringAfter(stateDelimiter,"")
                .substringBefore("/")
        if(state == ocdDivisionId) { // missingDelimiterValue
            // Accept ocd-division/country:us/district:dc format
            state = ocdDivisionId.substringAfter(districtDelimiter, "")
                .substringBefore("/")
            if (state == ocdDivisionId) { // missingDelimiterValue
                state = ""
            }
        }

        return Division(ocdDivisionId, country, state)
    }

    @ToJson
    fun divisionToJson (division: Division): String {
        return division.id
    }
}