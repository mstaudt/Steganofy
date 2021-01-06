package com.braffdev.steganofy.ui.reveal.wizard

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.lib.domain.EncryptionAlgorithm
import com.braffdev.steganofy.lib.domain.SteganoInfo
import com.braffdev.steganofy.lib.service.RevealService
import com.braffdev.steganofy.service.BitmapService
import com.braffdev.steganofy.service.ExecutionService
import com.braffdev.steganofy.ui.common.SingleLiveEvent
import com.braffdev.steganofy.ui.reveal.RevealActivity
import org.apache.commons.lang3.ArrayUtils
import java.io.ByteArrayInputStream

class RevealWizardViewModel(
    val applicationContext: Context,
    val revealService: RevealService,
    val bitmapService: BitmapService,
    val executionService: ExecutionService,
) {

    val liveDataStartEnabled = MutableLiveData(false)
    val liveDataInputState = MutableLiveData(RevealWizardState.NO_INPUT)
    val liveDataDecryptionPasswordShown = MutableLiveData(false)
    val liveDataStart = SingleLiveEvent<Intent>()
    var imageUri: Uri? = null
    var bitmap: Bitmap? = null
    var decryptionPassword: CharArray? = null
    var steganoInfo: SteganoInfo? = null

    fun startClicked() {
        if (imageUri != null) {
            liveDataStart.value = RevealActivity.createIntent(applicationContext, imageUri!!, decryptionPassword)
        }
    }

    fun imageUriChanged(imageUri: Uri) {
        this.imageUri = imageUri
        executionService.executeInBackground {
            bitmap = bitmapService.decodeBitmap(imageUri)
            checkSettings()
        }
    }

    fun decryptionPasswordChanged(decryptionPassword: CharArray) {
        this.decryptionPassword = decryptionPassword
        checkSettings()
    }

    private fun checkSettings() {
        executionService.executeInBackground {
            val inputStream = ByteArrayInputStream(bitmapService.getPixelBytes(bitmap!!))
            steganoInfo = inputStream.use { revealService.revealInfo(inputStream) }

            if (steganoInfo == null) {
                liveDataInputState.postValue(RevealWizardState.INPUT_INVALID)
            } else {
                liveDataInputState.postValue(RevealWizardState.INPUT_VALID)
            }

            val containsHiddenData = steganoInfo != null
            val isEncrypted = steganoInfo?.encryptionAlgorithm == EncryptionAlgorithm.AES_256
            liveDataDecryptionPasswordShown.postValue(isEncrypted)
            liveDataStartEnabled.postValue(containsHiddenData && ((isEncrypted && ArrayUtils.isNotEmpty(decryptionPassword)) || !isEncrypted))
        }
    }
}
