package com.braffdev.steganofy.ui.reveal.success

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.R
import com.braffdev.steganofy.lib.domain.Type
import com.braffdev.steganofy.ui.reveal.RevealViewModel
import org.apache.commons.io.FileUtils

class RevealSuccessViewModel(val applicationContext: Context, val parentViewModel: RevealViewModel) {

    val liveDataStatisticsBytes = MutableLiveData<String>()
    val liveDataStatisticsRevealTime = MutableLiveData<String>()
    val liveDataStatisticsImageProcessing = MutableLiveData<String>()
    val liveDataStatisticsTotalTime = MutableLiveData<String>()

    fun initialize() {
        val dataSize = FileUtils.byteCountToDisplaySize(parentViewModel.dataSizeInBytes)
        val statistics = parentViewModel.statistics

        liveDataStatisticsBytes.value = applicationContext.getString(R.string.reveal_statistics_bytes, dataSize)
        liveDataStatisticsRevealTime.value = applicationContext.getString(R.string.ms, statistics?.operationInMs)
        liveDataStatisticsImageProcessing.value = applicationContext.getString(R.string.ms, statistics?.imageProcessingInMs)
        liveDataStatisticsTotalTime.value = applicationContext.getString(R.string.ms, statistics?.totalTimeInMs)
    }

    fun getPayloadType(): Type {
        return parentViewModel.outputPayload!!.getType()
    }
}
