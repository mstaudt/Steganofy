package com.braffdev.steganofy.ui.reveal.success

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.lib.domain.FilePayload
import com.braffdev.steganofy.lib.domain.Type
import com.braffdev.steganofy.service.BitmapService
import com.braffdev.steganofy.service.ExecutionService
import com.braffdev.steganofy.service.FileService
import com.braffdev.steganofy.ui.reveal.RevealViewModel
import org.apache.commons.io.FileUtils

class RevealSuccessFileViewModel(
    val applicationContext: Context,
    val parentViewModel: RevealViewModel,
    val executionService: ExecutionService,
    val fileService: FileService,
    val bitmapService: BitmapService,
) {

    val liveDataImagePreview = MutableLiveData<Bitmap>()
    val liveDataFileInfoText = MutableLiveData<String>()
    lateinit var mimeType: String

    fun initialize() {
        val outputPayload = parentViewModel.outputPayload
        require(outputPayload != null) { "outputPayload must not be null" }
        require(outputPayload.getType() == Type.FILE) { "outputPayload must be of type FILE" }

        val filePayload = outputPayload as FilePayload
        mimeType = filePayload.mimeType

        if (mimeType.startsWith("image")) {
            executionService.executeInBackground {
                liveDataImagePreview.postValue(bitmapService.decodeBitmap(parentViewModel.temporaryOutputFileUri!!))
            }
        }

        val humanReadableFileSize = FileUtils.byteCountToDisplaySize(filePayload.file.size.toLong())
        liveDataFileInfoText.value = fileService.formatFileInfo(mimeType, humanReadableFileSize)
    }

    fun getTemporaryFileUri(): Uri {
        return parentViewModel.temporaryOutputFileUri!!
    }
}