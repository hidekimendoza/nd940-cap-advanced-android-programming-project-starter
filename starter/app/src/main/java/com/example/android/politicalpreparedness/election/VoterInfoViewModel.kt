package com.example.android.politicalpreparedness.election

import android.location.Address
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.network.models.VoterInfo
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val repository: ElectionRepository) : ViewModel() {

    // Add live data to hold voter info
    private var _voterInfo: MutableLiveData<VoterInfo?> = MutableLiveData(null)
    var voterInfo: LiveData<VoterInfo?> = _voterInfo

    private var electionId: Int? = null
    var election = MutableLiveData<ElectionDomainModel?>(null)

    val errorMessage: SingleLiveEvent<Int> = SingleLiveEvent()


    fun getElectionMainInfo(id: Int) {
        electionId = id
        Log.d("VoterInfoViewModel", "Main info provided id: $id")

        viewModelScope.launch {
            val result = repository.getElectionById(id)
            if (result.isSuccess) {
                election.value = result.getOrNull()
                Log.d("VoterInfoViewModel", "Set election main info from ${election.value}")
            } else {
                Log.w("VoterInfoViewModel", "Unable to find election to populate main info")
                election.value = null
            }
        }
    }


    // Add var and methods to support loading URLs
    // cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    // Add var and methods to save and remove elections to local database
    fun followElection() {
        Log.d("VoterInfoViewModel", "Following election ..")
        viewModelScope.launch {
            repository.followElectionState(election.value!!)
            getElectionMainInfo(electionId!!)
        }
    }

    fun unfollowElection() {
        Log.d("VoterInfoViewModel", "Unfollowing election ..")
        viewModelScope.launch {
            repository.unfollowElectionState(election.value!!)
            getElectionMainInfo(electionId!!)
        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    // Add var and methods to populate voter info
    fun getVoterInfo(address: Address) {
        if (electionId != null) {
            viewModelScope.launch {
                Log.d("VoterInfoViewModel", "getVoterInfo address:${address.getAddressLine(0)}")

                val result = repository.getElectionDetailsFromNetwork(
                    electionId.toString(),
                    address.getAddressLine(0).toString()
//                    addressDM.toFormattedString()
                )
                if (result.isSuccess) {
                    Log.d("VoterInfoViewModel", "getVoterInfo succeeded")
                    _voterInfo.value = result.getOrNull()
                } else {
                    Log.d("VoterInfoViewModel", "getVoterInfo Failed")
                    errorMessage.value = R.string.error_msg_fail_voter_info_check_network
                    _voterInfo.value = null
                }
            }
        } else {
            Log.e("VoterInfoViewModel", "electionId has not been initialized")
        }

    }
}