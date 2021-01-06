package com.braffdev.steganofy.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
class HideAndRevealPlainTextUITest : FlowTest() {

    @Test
    fun testHidePlainText() {
        // MainActivity
        onView(withText(R.string.main_welcome))
            .check(matches(isDisplayed()))

        // HideWizardActivity
        onView(withId(R.id.buttonHide))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.editTextText))
            .perform(scrollTo(), replaceText(TEST_MESSAGE), closeSoftKeyboard())

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

        onView(withId(R.id.buttonStart))
            .perform(scrollTo(), click())

        // HideActivity
        onView(withId(R.id.textViewStatisticsBytes))
            .check(matches(isDisplayed()))

        onView(withId(R.id.buttonSaveFile))
            .perform(scrollTo(), click())

        // Save the file
        FilePickerUtils.saveFile(device, "output-plaintext.png")

        // MainActivity
        onView(withText(R.string.main_welcome))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testRevealPlainText() {
        // MainActivity
        onView(withText(R.string.main_welcome))
            .check(matches(isDisplayed()))

        // RevealWizardActivity
        onView(withId(R.id.buttonReveal))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.selectFile))
            .check(matches(isDisplayed()))
            .perform(click())

        // File picker - select the image
        FilePickerUtils.selectFile(device, "output-plaintext.png")

        // Wait for the bitmap to be loaded
        Thread.sleep(1000)

        onView(withId(R.id.revealInfoText))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.reveal_info)))

        onView(withId(R.id.buttonStart))
            .perform(scrollTo(), click())

        // RevealActivity
        onView(withId(R.id.textViewStatisticsBytes))
            .check(matches(isDisplayed()))

        onView(withId(R.id.textViewResultPlainText))
            .check(matches(isDisplayed()))
            .check(matches(withText(TEST_MESSAGE)))

        onView(withId(R.id.buttonClose))
            .check(matches(isDisplayed()))
            .perform(click())

        // MainActivity
        onView(withText(R.string.main_welcome))
            .check(matches(isDisplayed()))
    }

    companion object {
        const val TEST_MESSAGE = "This is just a test"
    }


}
