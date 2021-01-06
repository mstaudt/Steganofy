package com.braffdev.steganofy.ui.hide.success

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.R
import com.braffdev.steganofy.ui.common.SingleLiveEvent
import com.braffdev.steganofy.ui.hide.HideViewModel
import org.apache.commons.io.FileUtils

class HideSuccessViewModel(val applicationContext: Context, val parentViewModel: HideViewModel) {

    val liveDataStatisticsBytes = MutableLiveData<String>()
    val liveDataStatisticsHideTime = MutableLiveData<String>()
    val liveDataStatisticsImageProcessing = MutableLiveData<String>()
    val liveDataStatisticsTotalTime = MutableLiveData<String>()
    val liveEventFinish = SingleLiveEvent<Void>()

    fun initialize() {
        val dataSize = FileUtils.byteCountToDisplaySize(parentViewModel.dataSizeInBytes)
        val statistics = parentViewModel.statistics

        liveDataStatisticsBytes.value = applicationContext.getString(R.string.hide_statistics_bytes, dataSize)
        liveDataStatisticsHideTime.value = applicationContext.getString(R.string.ms, statistics?.operationInMs)
        liveDataStatisticsImageProcessing.value = applicationContext.getString(R.string.ms, statistics?.imageProcessingInMs)
        liveDataStatisticsTotalTime.value = applicationContext.getString(R.string.ms, statistics?.totalTimeInMs)
    }

    fun getTemporaryFileUri(): Uri {
        return parentViewModel.temporaryOutputFileUri!!
    }

    fun imageSaved() {
        liveEventFinish.send()
    }
}
