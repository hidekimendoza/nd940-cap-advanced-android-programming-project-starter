package com.example.android.politicalpreparedness.election

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.util.MainCoroutineScopeRule
import com.example.android.politicalpreparedness.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.junit.Before
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class ElectionsViewModelTest{
    @get:Rule
    var coroutineTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository: ElectionRepository = mock(ElectionRepository::class.java)
    private val elections = mock(listOf<ElectionDomainModel>()::class.java)
    private val expected = Result.success(elections)
    private val exception = RuntimeException("Something when wrong")

    private lateinit var viewModel: ElectionsViewModel

    @Before
    fun setup(){
        viewModel =  ElectionsViewModel(ApplicationProvider.getApplicationContext(), repository)
    }

    @Test
    fun givenViewModel_whenSet_shouldGetElectionsFromRepository() = runBlockingTest{
//        verify(repository, times(1)).refreshElectionsFromNetwork()
    }

//    @Test
//    fun loadElection_LoadingState_shouldChangeWhenElectionRefreshed() = coroutineTestRule.runBlockingTest{
//        MatcherAssert.assertThat(viewModel.isLoadingDB.getOrAwaitValue(), `is`(false))
//        coroutineTestRule.pauseDispatcher()
//        viewModel.loadElections()
//        MatcherAssert.assertThat(viewModel.isLoadingDB.getOrAwaitValue(), `is`(true))
//        coroutineTestRule.resumeDispatcher()
//        MatcherAssert.assertThat(viewModel.isLoadingDB.getOrAwaitValue(), `is`(false))
//    }
}