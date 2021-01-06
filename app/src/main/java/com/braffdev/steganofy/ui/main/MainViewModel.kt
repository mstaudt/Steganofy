package com.braffdev.steganofy.ui.main

import android.content.Context
import android.content.Intent
import com.braffdev.steganofy.service.ExecutionService
import com.braffdev.steganofy.ui.about.AboutActivity
import com.braffdev.steganofy.ui.common.SingleLiveEvent
import com.braffdev.steganofy.ui.hide.wizard.HideWizardActivity
import com.braffdev.steganofy.ui.reveal.wizard.RevealWizardActivity

class MainViewModel(val applicationContext: Context, val executionService: ExecutionService) {

    val liveEventStartActivity = SingleLiveEvent<Intent>()
    val liveEventStartAnimation = SingleLiveEvent<Void>()
    val liveEventStopAnimation = SingleLiveEvent<Void>()

    fun hideClicked() {
        liveEventStartActivity.postValue(Intent(applicationContext, HideWizardActivity::class.java))
    }

    fun revealClicked() {
        liveEventStartActivity.postValue(Intent(applicationContext, RevealWizardActivity::class.java))
    }

    fun aboutClicked() {
        liveEventStartActivity.postValue(Intent(applicationContext, AboutActivity::class.java))
    }

    fun animationEnded() {
        startAnimationDelayed()
    }

    fun resume() {
        liveEventStopAnimation.send()
        startAnimationDelayed()
    }

    private fun startAnimationDelayed() {
        executionService.executeInBackground {
            Thread.sleep(1750) // The time between the background & foreground animation
            liveEventStartAnimation.send()
        }
    }
}