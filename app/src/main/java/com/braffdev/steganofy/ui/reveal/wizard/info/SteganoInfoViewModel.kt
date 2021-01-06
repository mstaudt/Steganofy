package com.braffdev.steganofy.ui.reveal.wizard.info

import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.R
import com.braffdev.steganofy.lib.domain.EncryptionAlgorithm
import com.braffdev.steganofy.lib.domain.SteganoInfo

class SteganoInfoViewModel {

    val liveDataTextRes = MutableLiveData<Int>()
    val liveDataVersion = MutableLiveData<String>()
    val liveDataTypeTextRes = MutableLiveData<Int>()
    val liveDataEncryptionTextRes = MutableLiveData<Int>()

    fun initialize(steganoInfo: SteganoInfo) {
        if (steganoInfo.encryptionAlgorithm == EncryptionAlgorithm.NONE) {
            liveDataTextRes.value = R.string.reveal_info
        } else {
            liveDataTextRes.value = R.string.reveal_info_encrypted
        }

        liveDataVersion.value = steganoInfo.version.toString()

        if (steganoInfo.payloadType == com.braffdev.steganofy.lib.domain.Type.PLAINTEXT) {
            liveDataTypeTextRes.value = R.string.plaintext

        } else if (steganoInfo.payloadType == com.braffdev.steganofy.lib.domain.Type.FILE) {
            liveDataTypeTextRes.value = R.string.file
        }

        if (steganoInfo.encryptionAlgorithm == EncryptionAlgorithm.NONE) {
            liveDataEncryptionTextRes.value = R.string.encryption_none

        } else if (steganoInfo.encryptionAlgorithm == EncryptionAlgorithm.AES_256) {
            liveDataEncryptionTextRes.value = R.string.encryption_aes
        }
    }
}
