package com.braffdev.steganofy.ui.common.message

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.R
import com.braffdev.steganofy.ui.common.message.MessageFragment.Type

class MessageViewModel {

    val liveDataTextRes = MutableLiveData<Int>()
    val liveDataBackgroundRes = MutableLiveData<Int>()
    val liveDataIconRes = MutableLiveData<Int>()

    fun initialize(@StringRes textRes: Int, type: Type) {
        liveDataTextRes.value = textRes

        if (type == Type.ERROR) {
            liveDataIconRes.value = R.drawable.baseline_error_24
            liveDataBackgroundRes.value = R.drawable.message_error

        } else if (type == Type.INFO) {
            liveDataIconRes.value = R.drawable.baseline_info_24
            liveDataBackgroundRes.value = R.drawable.message_info
        }
    }
}
