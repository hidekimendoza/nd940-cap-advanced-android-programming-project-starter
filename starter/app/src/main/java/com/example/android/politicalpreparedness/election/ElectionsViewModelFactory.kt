package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.repository.ElectionRepository

//// Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory(private val app: Application, private val repository: ElectionRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ElectionsViewModel(app, repository) as T
    }


}