package com.braffdev.steganofy.ui


import androidx.test.espresso.Espresso.onView
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
class HideAndRevealFileUITest : FlowTest() {

    @Test
    fun testHideFile() {
        // MainActivity
        onView(withText(R.string.main_welcome))
            .check(matches(isDisplayed()))

        // HideWizardActivity
        onView(withId(R.id.buttonHide))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.radioButtonFile))
            .perform(click())

        onView(withId(R.id.selectFile))
            .check(matches(isDisplayed()))
            .perform(click())

        // File picker - select the image
        FilePickerUtils.selectFile(device, "image_20x20.jpg")

        // Wait for the bitmap to be loaded
        Thread.sleep(1000)

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
        FilePickerUtils.saveFile(device, "output-file.png")

        // MainActivity
        onView(withText(R.string.main_welcome))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testRevealFile() {
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
        FilePickerUtils.selectFile(device, "output-file.png")

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

        onView(withId(R.id.textViewFileInfo))
            .check(matches(isDisplayed()))
            .check(matches(withText("image/jpeg, 881 bytes")))

        onView(withId(R.id.imageViewPreview))
            .check(matches(isDisplayed()))

        onView(withId(R.id.buttonSaveFile))
            .perform(scrollTo(), click())

        // File Picker
        FilePickerUtils.saveFile(device, "extracted-image_20x20.jpg")

        // MainActivity
        onView(withText(R.string.main_welcome))
            .check(matches(isDisplayed()))
    }
}
