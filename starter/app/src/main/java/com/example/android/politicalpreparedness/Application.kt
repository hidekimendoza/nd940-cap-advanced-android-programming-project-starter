package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.source.ElectionDataSource
import com.example.android.politicalpreparedness.source.local.LocalElectionDataSource
import com.example.android.politicalpreparedness.source.remote.RemoteElectionDataSource
import kotlinx.coroutines.Dispatchers

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = module {
            viewModel { ElectionsViewModel(this@MyApp, get())}
//            viewModel { RepresentativeViewModel(get()) }
            single { ElectionDatabase.getInstance(this@MyApp)}
            single { CivicsApi.retrofitService as CivicsApiService }

            single(qualifier = named("local")) { LocalElectionDataSource(get(), Dispatchers.IO) }
            single(qualifier = named("remote")) { RemoteElectionDataSource(get(), Dispatchers.IO) }
            single {
                ElectionRepository(
                    get<ElectionDataSource>(qualifier = named("local")) as LocalElectionDataSource,
                    get<ElectionDataSource>(qualifier = named("remote")) as RemoteElectionDataSource,
                    Dispatchers.IO,
                )
            }
        }
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(module))
        }
    }
}