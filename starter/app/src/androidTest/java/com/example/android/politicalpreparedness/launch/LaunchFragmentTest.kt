package com.example.android.politicalpreparedness.launch

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.politicalpreparedness.R
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@RunWith(AndroidJUnit4::class)
class LaunchFragmentTest{
//    @Test
//    fun uiElements_onLaunch_shouldBeDisplayed(){
//        val scenario = launchFragmentInContainer<LaunchFragment>(Bundle(), R.style.AppTheme)
//        onView(withId(R.id.imageViewBallotLogo)).check(matches(isDisplayed()))
//    }
////
////
//    @Test
//    fun electionButton_onClick_shouldNavigateToElectionFragment(){
//        val navController = mock(NavController::class.java)
//        val activityScenario = launchFragmentInContainer<LaunchFragment>(Bundle(), R.style.AppTheme)
//        onView(withText(R.string.upcoming_elections)).perform(click())
//        verify(navController).navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())
////        assertThat(
////            1,
////            `is`(1)
////        )
//    }

}