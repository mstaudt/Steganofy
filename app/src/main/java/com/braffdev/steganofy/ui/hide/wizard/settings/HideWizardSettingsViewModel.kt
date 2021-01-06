package com.braffdev.steganofy.ui.hide.wizard.settings

import android.content.Context
import android.content.Intent
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.R
import com.braffdev.steganofy.lib.domain.EncryptionAlgorithm
import com.braffdev.steganofy.lib.domain.SteganoData
import com.braffdev.steganofy.lib.service.HideService
import com.braffdev.steganofy.service.ExecutionService
import com.braffdev.steganofy.ui.common.SingleLiveEvent
import com.braffdev.steganofy.ui.hide.HideActivity
import com.braffdev.steganofy.ui.hide.wizard.HideWizardViewModel
import org.apache.commons.lang3.ArrayUtils

class HideWizardSettingsViewModel(
    val applicationContext: Context,
    val parentViewModel: HideWizardViewModel,
    val hideService: HideService,
    val executionService: ExecutionService,
) {

    val liveDataStart = SingleLiveEvent<Intent>()
    val liveDataEncryptionPasswordShown = MutableLiveData(false)
    val liveDataStartButtonEnabled = MutableLiveData(true)
    val liveDataShowFileTooSmall = MutableLiveData(false)

    var encryptionAlgorithm = EncryptionAlgorithm.NONE
    var encryptionPassword: CharArray? = null

    fun radioButtonChecked(@IdRes checkedId: Int) {
        encryptionAlgorithm = if (checkedId == R.id.radioButtonAESEncryption) {
            EncryptionAlgorithm.AES_256
        } else {
            EncryptionAlgorithm.NONE
        }

        liveDataEncryptionPasswordShown.value = encryptionAlgorithm == EncryptionAlgorithm.AES_256
        checkSettings()
    }

    fun encryptionPasswordChanged(password: CharArray) {
        encryptionPassword = password
        checkSettings()
    }

    fun previousClicked() {
        parentViewModel.previousClicked()
    }

    fun start() {
        parentViewModel.encryptionAlgorithmChanged(encryptionAlgorithm)
        if (encryptionAlgorithm != EncryptionAlgorithm.NONE) {
            parentViewModel.encryptionPasswordChanged(encryptionPassword)
        }

        parentViewModel.start()
        liveDataStart.value = HideActivity.createIntent(applicationContext)
    }

    private fun checkSettings() {
        if (encryptionAlgorithm == EncryptionAlgorithm.NONE) {
            liveDataStartButtonEnabled.value = true
            return
        }

        executionService.executeInBackground {
            val imageLargeEnough = hideService.isImageLargeEnough(
                parentViewModel.bitmap!!.allocationByteCount,
                SteganoData(payload = parentViewModel.payload!!,
                    encryptionAlgorithm = encryptionAlgorithm,
                    encryptionPassword = "Settings".toCharArray())
            )

            liveDataStartButtonEnabled.postValue(imageLargeEnough && ArrayUtils.isNotEmpty(encryptionPassword))
            liveDataShowFileTooSmall.postValue(!imageLargeEnough)
        }
    }
}