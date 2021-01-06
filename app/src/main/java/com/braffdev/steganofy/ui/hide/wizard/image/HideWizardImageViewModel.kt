package com.braffdev.steganofy.ui.hide.wizard.image

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.lib.domain.Payload
import com.braffdev.steganofy.lib.domain.SteganoData
import com.braffdev.steganofy.lib.service.HideService
import com.braffdev.steganofy.service.BitmapService
import com.braffdev.steganofy.service.ExecutionService
import com.braffdev.steganofy.service.FileService
import com.braffdev.steganofy.ui.hide.wizard.HideWizardViewModel

class HideWizardImageViewModel(
    val parentViewModel: HideWizardViewModel,
    val hideService: HideService,
    val bitmapService: BitmapService,
    val fileService: FileService,
    val executionService: ExecutionService,
) {

    val liveDataShowImageTooSmall = MutableLiveData(false)
    val liveDataShowImageTooLarge = MutableLiveData(false)
    val liveDataNextButtonEnabled = MutableLiveData(false)
    var imageUri: Uri? = null

    fun imageUriChanged(imageUri: Uri) {
        this.imageUri = imageUri

        executionService.executeInBackground {
            if (isImageTooLarge(imageUri)) {
                liveDataShowImageTooLarge.postValue(true)
                liveDataShowImageTooSmall.postValue(false)
                liveDataNextButtonEnabled.postValue(false)

            } else {
                liveDataShowImageTooLarge.postValue(false)
                parentViewModel.imageChanged(bitmapService.decodeBitmap(imageUri))
                checkImageIsLargeEnough()
            }
        }
    }

    fun resume() {
        checkImageIsLargeEnough()
    }

    fun previousClicked() {
        parentViewModel.previousClicked()
    }

    fun nextClicked() {
        parentViewModel.nextClicked()
    }

    private fun isImageTooLarge(imageUri: Uri): Boolean {
        val fileSizeInBytes = fileService.getFileSize(imageUri)
        return fileSizeInBytes > MAX_FILE_SIZE
    }

    private fun getPayload(): Payload {
        return parentViewModel.payload!!
    }

    private fun checkImageIsLargeEnough() {
        if (parentViewModel.bitmap != null) {
            executionService.executeInBackground {
                val imageLargeEnough = hideService.isImageLargeEnough(
                    parentViewModel.bitmap!!.allocationByteCount,
                    SteganoData(getPayload())
                )

                liveDataNextButtonEnabled.postValue(imageLargeEnough)
                liveDataShowImageTooSmall.postValue(!imageLargeEnough)
            }
        }
    }

    companion object {
        /**
         * 25 MB
         */
        const val MAX_FILE_SIZE = 25 * 1000 * 1000
    }
}