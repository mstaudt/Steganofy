package com.braffdev.steganofy.ui.internal

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector


class FilePickerUtils {

    companion object {

        fun selectFile(device: UiDevice, fileName: String) {
            // Open search
            if (android.os.Build.VERSION.SDK_INT <= 26) {
                device.findObject(UiSelector().resourceIdMatches(".*id/menu_search")).click()
            } else {
                device.findObject(UiSelector().resourceIdMatches(".*id/option_menu_search")).click()
            }

            // Search for "steganofy-test". That's the folder containing the test images. See build.gradle for more info
            device.findObject(UiSelector().resourceIdMatches(".*id/search_src_text")).text = "steganofy-test"

            if (android.os.Build.VERSION.SDK_INT <= 26) {
                device.pressEnter()
            }

            // Select the folder
            device.findObject(UiSelector().resourceId("android:id/title").text("steganofy-test")).click()

            // Select the desired image
            device.findObject(UiSelector().text(fileName)).click()
        }

        fun saveFile(device: UiDevice, fileName: String) {
            // Check if the file was already saved
            val alreadySavedFile = device.findObject(UiSelector()
                .resourceId("android:id/title")
                .className("android.widget.TextView")
                .text(fileName))

            val alreadySavedFileExisted = alreadySavedFile.exists()
            if (alreadySavedFileExisted) {
                // If already saved - select to overwrite the file. Otherwise a file ".. (1).png" would be created
                alreadySavedFile.click()

            } else {
                // File does not exist yet - enter the desired file name
                device.findObject(UiSelector().resourceId("android:id/title").className("android.widget.EditText")).text = fileName
            }

            // Save
            device.findObject(UiSelector().resourceId("android:id/button1")).click()

            if (alreadySavedFileExisted) {
                // Accept warning to overwrite the file
                device.findObject(UiSelector().text("OK")).click()
            }
        }
    }
}