package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse (
    val election: Election,
    val pollingLocations: String? = null, //TODO: Future Use
    val contests: String? = null, //TODO: Future Use
    val state: List<State>? = null,
    val electionElectionOfficials: List<ElectionOfficial>? = null
)

data class VoterInfo(
    val election: Election,
    val state: List<State>?,
    val electionElectionOfficials: List<ElectionOfficial>?
)

fun VoterInfoResponse.asDomainModel(): VoterInfo{
    return VoterInfo(
        election= this.election,
        state = this.state,
        electionElectionOfficials = this.electionElectionOfficials
    )
}