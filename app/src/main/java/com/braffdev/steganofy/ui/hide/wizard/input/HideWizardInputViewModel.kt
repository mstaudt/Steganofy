package com.braffdev.steganofy.ui.hide.wizard.input

import android.net.Uri
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.R
import com.braffdev.steganofy.service.ExecutionService
import com.braffdev.steganofy.service.PayloadService
import com.braffdev.steganofy.ui.hide.wizard.HideWizardViewModel
import org.apache.commons.lang3.StringUtils

class HideWizardInputViewModel(
    val parentViewModel: HideWizardViewModel,
    val payloadService: PayloadService,
    val executionService: ExecutionService,
) {

    val liveDataPlainTextShown = MutableLiveData(true)
    val liveDataSelectFileShown = MutableLiveData(false)
    val liveDataNextButtonEnabled = MutableLiveData(false)
    var plainText: String? = null
    var file: Uri? = null

    fun nextClicked() {
        parentViewModel.nextClicked()
        executionService.executeInBackground {
            // Construction of payload reads the bitmaps. As this may take some time we should do it in background
            parentViewModel.payloadChanged(payloadService.createPayload(plainText, file))
        }
    }

    fun radioButtonChecked(@IdRes checkedId: Int) {
        liveDataPlainTextShown.value = checkedId == R.id.radioButtonPlaintext
        liveDataSelectFileShown.value = checkedId == R.id.radioButtonFile
        checkNecessaryValues()
    }

    fun payloadPlainTextChanged(plainText: String?) {
        this.plainText = plainText
        checkNecessaryValues()
    }

    fun payloadFileChanged(file: Uri) {
        this.file = file
        checkNecessaryValues()
    }

    private fun checkNecessaryValues() {
        val plainTextEntered = liveDataPlainTextShown.value!! && StringUtils.isNotBlank(plainText)
        val fileSelected = liveDataSelectFileShown.value!! && file != null
        liveDataNextButtonEnabled.value = plainTextEntered || fileSelected
    }
}