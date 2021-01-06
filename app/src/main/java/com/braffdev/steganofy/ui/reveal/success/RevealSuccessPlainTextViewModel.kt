package com.braffdev.steganofy.ui.reveal.success

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.braffdev.steganofy.lib.domain.PlainTextPayload
import com.braffdev.steganofy.lib.domain.Type
import com.braffdev.steganofy.ui.reveal.RevealViewModel

class RevealSuccessPlainTextViewModel(val applicationContext: Context, val parentViewModel: RevealViewModel) {

    val liveDataPlainText = MutableLiveData<String>()

    fun initialize() {
        liveDataPlainText.value = getPlainText()
    }

    fun copyToClipBoardClicked() {
        val clipboardManager = ContextCompat.getSystemService(applicationContext, ClipboardManager::class.java)
        clipboardManager?.setPrimaryClip(ClipData.newPlainText("plaintext", getPlainText()))
    }

    private fun getPlainText(): String {
        val outputPayload = parentViewModel.outputPayload
        require(outputPayload != null) { "outputPayload must not be null" }
        require(outputPayload.getType() == Type.PLAINTEXT) { "outputPayload must be of type PLAINTEXT" }
        return (outputPayload as PlainTextPayload).plaintext
    }
}