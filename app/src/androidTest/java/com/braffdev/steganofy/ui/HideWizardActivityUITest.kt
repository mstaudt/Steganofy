package com.braffdev.steganofy.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.braffdev.steganofy.R
import com.braffdev.steganofy.ui.internal.FilePickerUtils
import com.braffdev.steganofy.ui.internal.FlowTest
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HideWizardActivityUITest : FlowTest() {

    @Test
    fun testBackPressBehavior() {
        // MainActivity
        onView(withText(R.string.main_welcome))
            .check(matches(isDisplayed()))

        // HideWizardActivity
        onView(withId(R.id.buttonHide))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.editTextText))
            .perform(scrollTo(), ViewActions.replaceText(HideAndRevealPlainTextUITest.TEST_MESSAGE), ViewActions.closeSoftKeyboard())

        onView(withId(R.id.next))
            .perform(scrollTo(), click())

        onView(withId(R.id.selectFile))
            .check(matches(isDisplayed()))
            .perform(click())

        // File picker - select the image
        FilePickerUtils.selectFile(device, "image_100x100.png")

        // Wait for the bitmap to be loaded
        Thread.sleep(1000)

        onView(withId(R.id.next))
            .perform(scrollTo(), click())

        onView(withText(R.string.hide_settings))
            .check(matches(isDisplayed()))

        pressBack()

        onView(withText(R.string.hide_image))
            .check(matches(isDisplayed()))

        pressBack()

        onView(withText(R.string.hide_input))
            .check(matches(isDisplayed()))

        pressBack()

        onView(withText(R.string.cancel_confirmation))
            .check(matches(isDisplayed()))

        onView(withText(R.string.yes))
            .perform(click())

        // MainActivity
        onView(withText(R.string.main_welcome))
            .check(matches(isDisplayed()))
    }
}
