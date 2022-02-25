package com.example.android.politicalpreparedness.network

import androidx.test.runner.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CivicsApiTest {


    @Test
    fun getElections() = runBlocking {
        val api = CivicsApi
        val elections = api.retrofitService.getElections()
        assert(true)
    }

}