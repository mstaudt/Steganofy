package com.braffdev.steganofy.ui.hide.wizard

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.lib.domain.EncryptionAlgorithm
import com.braffdev.steganofy.lib.domain.Payload
import com.braffdev.steganofy.lib.domain.SteganoData
import com.braffdev.steganofy.ui.common.SingleLiveEvent
import com.braffdev.steganofy.ui.hide.HideDataBridge

class HideWizardViewModel(val hideDataBridge: HideDataBridge) {

    val liveDataCurrentPage = MutableLiveData(0)
    val liveEventShowCancelDialog = SingleLiveEvent<Void>()
    var payload: Payload? = null
    var bitmap: Bitmap? = null
    var encryptionAlgorithm = EncryptionAlgorithm.NONE
    var encryptionPassword: CharArray? = null

    fun start() {
        // Pass the data to the activity actually running the operation
        hideDataBridge.bitmap = bitmap
        hideDataBridge.steganoData =
            SteganoData(payload = payload!!, encryptionAlgorithm = encryptionAlgorithm, encryptionPassword = encryptionPassword)
    }

    fun imageChanged(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    fun nextClicked() {
        val currentValue = liveDataCurrentPage.value!!
        if (currentValue < 2) {
            liveDataCurrentPage.value = currentValue + 1
        }
    }

    fun previousClicked() {
        val currentValue = liveDataCurrentPage.value!!
        if (currentValue > 0) {
            liveDataCurrentPage.value = currentValue - 1
        } else {
            liveEventShowCancelDialog.send()
        }
    }

    fun encryptionAlgorithmChanged(encryptionAlgorithm: EncryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm
    }

    fun encryptionPasswordChanged(encryptionPassword: CharArray?) {
        this.encryptionPassword = encryptionPassword
    }

    fun payloadChanged(payload: Payload) {
        this.payload = payload
    }
}