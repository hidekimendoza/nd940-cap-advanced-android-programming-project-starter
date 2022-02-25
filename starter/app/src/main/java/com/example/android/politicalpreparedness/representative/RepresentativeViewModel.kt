package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository: ElectionRepository) : ViewModel() {
    // Establish live data for representatives and address

    val line1 = MutableLiveData("")
    val line2 = MutableLiveData("")
    val city = MutableLiveData("")
    val state = MutableLiveData("")
    val zip = MutableLiveData("")

    val messageInput: SingleLiveEvent<Int> = SingleLiveEvent()

    private val _representatives: MutableLiveData<List<Representative>> = MutableLiveData(listOf())
    val representatives: LiveData<List<Representative>> = _representatives

    private val _loadingRepresentatives = MutableLiveData(false)
    val loadingRepresentatives: LiveData<Boolean> = _loadingRepresentatives

    private fun showLoadingRepresentative() {
        _loadingRepresentatives.value = true
    }

    private fun hideLoadingRepresentative() {
        _loadingRepresentatives.value = false
    }

    // Create function get address from geo location
    fun updateAddressFields(inputAddress: Address) {
        line1.value = inputAddress.line1
        line2.value = inputAddress.line2.orEmpty()
        city.value = inputAddress.city
        state.value = inputAddress.state
        zip.value = inputAddress.zip
    }

    // Create function to get address from individual fields
    fun searchRepresentatives() {
        if (line1.value.isNullOrBlank()) {
            messageInput.value = R.string.error_missing_first_line_address
            return
        }
        if (city.value.isNullOrBlank()) {
            messageInput.value = R.string.error_missing_city
            return
        }
        if (state.value.isNullOrBlank()) {
            messageInput.value = R.string.error_missing_state
            return
        }
        if (zip.value.isNullOrBlank()) {
            messageInput.value = R.string.error_missing_zip
            return
        }
        val address = Address(
            requireNotNull(line1.value),
            line2.value,
            requireNotNull(city.value),
            requireNotNull(state.value),
            requireNotNull(zip.value)
        )
        Log.d("RepresentativeViewModel", "Search representatives with address $address")
        fetchRepresentatives(address)
    }


    // Create function to fetch representatives from API from a provided address
    private fun fetchRepresentatives(address: Address) {
        showLoadingRepresentative()
        viewModelScope.launch {
            val result = repository.getRepresentatives(address)
            if (result.isSuccess) {
                _representatives.postValue(result.getOrDefault(listOf()))
            } else {
                _representatives.postValue(listOf())
                messageInput.value = R.string.error_fetch_representatives_network
            }
            Log.d("fetchRepresentatives", "${_representatives.value}")

        }
        hideLoadingRepresentative()
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    fun setState(selectedState: String?) {
        state.value = selectedState.orEmpty()
    }

}
