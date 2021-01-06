package com.braffdev.steganofy.ui.hide

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.lib.service.HideService
import com.braffdev.steganofy.service.BitmapService
import com.braffdev.steganofy.service.ExecutionService
import com.braffdev.steganofy.service.FileService
import com.braffdev.steganofy.ui.common.OperationStatistics
import com.braffdev.steganofy.ui.common.OperationStatus
import com.braffdev.steganofy.ui.common.SingleLiveEvent
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class HideViewModel(
    val hideService: HideService,
    val hideDataBridge: HideDataBridge,
    val bitmapService: BitmapService,
    val fileService: FileService,
    val executionService: ExecutionService,
) {

    val liveDataStatus = MutableLiveData(OperationStatus.RUNNING)
    val liveEventCancelWarning = SingleLiveEvent<Void>()
    var temporaryOutputFileUri: Uri? = null
    var statistics: OperationStatistics? = null
    var dataSizeInBytes: Long = 0

    fun initialize() {
        val bitmap = hideDataBridge.bitmap
        require(bitmap != null) { "Bitmap must not be null" }

        val steganoData = hideDataBridge.steganoData
        require(steganoData != null) { "SteganoData must not be null" }

        // Release all resources from the bridge
        hideDataBridge.clear()

        executionService.executeInBackground {
            try {
                // Get input file. Unfortunately, there is currently no way to get the bitmap pixels as stream
                // hence we have to create it on our own
                val startTime = System.currentTimeMillis()
                val inputStream = ByteArrayInputStream(bitmapService.getPixelBytes(bitmap))
                val outputStream = ByteArrayOutputStream()

                // Hide the data
                val startTimeHide = System.currentTimeMillis()
                inputStream.use { outputStream.use { hideService.hide(inputStream, outputStream, steganoData) } }
                val hideTimeInMs = System.currentTimeMillis() - startTimeHide

                // Create a PNG bitmap of the pixels containing the data
                val outputBitmap = bitmapService.createBitmapFromPixelBytes(outputStream.toByteArray(), bitmap.width, bitmap.height)
                val output = bitmapService.compressToPNG(outputBitmap)

                // Write the PNG bitmap to the internal tmp dir
                temporaryOutputFileUri = fileService.createTemporaryFile("image/png", output)

                // Create statistics and finish
                statistics = OperationStatistics(System.currentTimeMillis() - startTime, hideTimeInMs)
                dataSizeInBytes = steganoData.getEstimatedLengthInBytes().toLong()
                liveDataStatus.postValue(OperationStatus.SUCCESS)

            } catch (e: Exception) {
                Log.e("HideViewModel", e.message, e)
                liveDataStatus.postValue(OperationStatus.ERROR)
            }
        }
    }

    fun finish() {
        if (temporaryOutputFileUri != null) {
            fileService.deleteTemporaryFile(temporaryOutputFileUri!!)
        }
    }

    fun backPressed() {
        if (liveDataStatus.value == OperationStatus.RUNNING || liveDataStatus.value == OperationStatus.SUCCESS) {
            liveEventCancelWarning.send()
        }
    }
}