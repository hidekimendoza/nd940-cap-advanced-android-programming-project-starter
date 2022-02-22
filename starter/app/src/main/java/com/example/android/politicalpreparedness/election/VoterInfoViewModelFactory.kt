package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.repository.ElectionRepository

// Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(private val repository: ElectionRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VoterInfoViewModel(repository) as T
    }

}