package com.braffdev.steganofy.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.braffdev.steganofy.R
import com.braffdev.steganofy.ui.about.AboutActivity
import org.junit.Rule
import org.junit.Test

class AboutActivityUITest {

    @get:Rule
    var activityRule = ActivityScenarioRule(AboutActivity::class.java)

    @Test
    fun testAbout() {
        onView(withId(R.id.textViewAboutPunchline))
            .check(matches(isDisplayed()))

        onView(withId(R.id.buttonAboutEmail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.buttonAboutWeb))
            .check(matches(isDisplayed()))

        onView(withId(R.id.buttonAboutGitHub))
            .check(matches(isDisplayed()))

        onView(withId(R.id.textViewAboutFAQ))
            .check(matches(isDisplayed()))

        onView(withId(R.id.textViewAboutLicenses))
            .check(matches(isDisplayed()))
    }
}