package com.braffdev.steganofy.ui.reveal

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.lib.domain.FilePayload
import com.braffdev.steganofy.lib.domain.Type
import com.braffdev.steganofy.lib.service.RevealService
import com.braffdev.steganofy.service.BitmapService
import com.braffdev.steganofy.service.ExecutionService
import com.braffdev.steganofy.service.FileService
import com.braffdev.steganofy.ui.common.OperationStatistics
import com.braffdev.steganofy.ui.common.OperationStatus
import com.braffdev.steganofy.ui.common.SingleLiveEvent
import java.io.ByteArrayInputStream

class RevealViewModel(
    val revealService: RevealService,
    val bitmapService: BitmapService,
    val fileService: FileService,
    val executionService: ExecutionService,
) {

    val liveDataStatus = MutableLiveData(OperationStatus.RUNNING)
    val liveEventCancelWarning = SingleLiveEvent<Void>()
    var outputPayload: com.braffdev.steganofy.lib.domain.Payload? = null
    var temporaryOutputFileUri: Uri? = null
    var statistics: OperationStatistics? = null
    var dataSizeInBytes: Long = 0

    fun initialize(imageUri: Uri, decryptionPassword: CharArray?) {
        executionService.executeInBackground {
            try {
                // Get input file. Unfortunately, there is currently no way to get the bitmap pixels as stream
                // hence we have to create it on our own
                val startTime = System.currentTimeMillis()

                val bitmap = bitmapService.decodeBitmap(imageUri)
                val inputStream = ByteArrayInputStream(bitmapService.getPixelBytes(bitmap))

                // Reveal the data
                val startTimeReveal = System.currentTimeMillis()
                val data = inputStream.use { revealService.reveal(inputStream, decryptionPassword) }
                val revealTimeInMs = System.currentTimeMillis() - startTimeReveal
                outputPayload = data.payload

                if (outputPayload?.getType() == Type.FILE) {
                    val filePayload = outputPayload as FilePayload
                    temporaryOutputFileUri = fileService.createTemporaryFile(filePayload.mimeType, filePayload.file)
                }

                // Create statistics and finish
                statistics = OperationStatistics(System.currentTimeMillis() - startTime, revealTimeInMs)
                dataSizeInBytes = data.getEstimatedLengthInBytes().toLong()
                liveDataStatus.postValue(OperationStatus.SUCCESS)

            } catch (e: Exception) {
                Log.e("RevealViewModel", e.message, e)
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