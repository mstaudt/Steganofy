package com.braffdev.steganofy.ui.main

import android.content.Intent
import androidx.lifecycle.Observer
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.InjectMocks

class MainViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: MainViewModel

    @Test
    fun testHideClicked() {
        // WHEN
        viewModel.hideClicked()

        // THEN
        assertThat(viewModel.liveEventStartActivity.value).isNotNull
        assertThat(viewModel.liveEventStartActivity.value).isInstanceOf(Intent::class.java)
    }

    @Test
    fun testRevealClicked() {
        // WHEN
        viewModel.revealClicked()

        // THEN
        assertThat(viewModel.liveEventStartActivity.value).isNotNull
        assertThat(viewModel.liveEventStartActivity.value).isInstanceOf(Intent::class.java)
    }

    @Test
    fun testAboutClicked() {
        // WHEN
        viewModel.aboutClicked()

        // THEN
        assertThat(viewModel.liveEventStartActivity.value).isNotNull
        assertThat(viewModel.liveEventStartActivity.value).isInstanceOf(Intent::class.java)
    }

    @Test
    fun testResume() {
        // GIVEN
        val stopAnimationObserver: Observer<Any> = mock()
        val startAnimationObserver: Observer<Any> = mock()
        viewModel.liveEventStopAnimation.observeForever(stopAnimationObserver)
        viewModel.liveEventStartAnimation.observeForever(startAnimationObserver)

        // WHEN
        viewModel.resume()

        // THEN
        verify(stopAnimationObserver).onChanged(anyOrNull())
        Thread.sleep(2000)
        verify(startAnimationObserver).onChanged(anyOrNull())
    }

    @Test
    fun testAnimationEnded() {
        // GIVEN
        val observer: Observer<Any> = mock()
        viewModel.liveEventStartAnimation.observeForever(observer)

        // WHEN
        viewModel.animationEnded()

        // THEN
        Thread.sleep(2000)
        verify(observer).onChanged(anyOrNull())
    }
}