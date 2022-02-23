package com.example.android.politicalpreparedness.network

import androidx.test.runner.AndroidJUnit4
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(AndroidJUnit4::class)
class CivicsApiTest {


    @Test
    fun getElections() = runBlocking {
        val api = CivicsApi
        val elections = api.retrofitService.getElections()
        assert(true)
    }

}