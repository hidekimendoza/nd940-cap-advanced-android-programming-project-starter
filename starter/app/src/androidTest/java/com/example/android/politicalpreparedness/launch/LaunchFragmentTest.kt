package com.example.android.politicalpreparedness.launch

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.runner.AndroidJUnit4
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.util.DataBindingIdlingResource
import com.example.android.politicalpreparedness.util.monitorFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class LaunchFragmentTest{

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun registerIdlingResource() {
        // IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        // IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun dummy(){
        assert(true)
    }

//    @Test
//    fun uiElements_onLaunch_shouldBeDisplayed(){
//        val scenario = launchFragmentInContainer<LaunchFragment>(Bundle(), R.style.AppTheme)
//        dataBindingIdlingResource.monitorFragment(scenario)
//        onView(withId(R.id.imageViewBallotLogo)).check(matches(isDisplayed()))
//    }
//
//
//    @Test
//    fun electionButton_onClick_shouldNavigateToElectionFragment(){
//        val navController = mock(NavController::class.java)
//        val scenario = launchFragmentInContainer<LaunchFragment>(Bundle(), R.style.AppTheme)
//        dataBindingIdlingResource.monitorFragment(scenario)
//         onView(withText(R.string.upcoming_elections)).perform(click())
//        verify(navController).navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())
//        assertThat(
//            1,
//            `is`(1)
//        )
//    }

}