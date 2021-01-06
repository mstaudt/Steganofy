package com.braffdev.steganofy.ui.about

import android.content.Intent
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.any
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`

class AboutViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: AboutViewModel

    @Test
    fun testInitialize() {
        // GIVEN
        val theText = "Version 1.0, Build 1"
        `when`(applicationContext.getString(any(), any())).thenReturn(theText)

        // WHEN
        viewModel.initialize()

        // THEN
        assertThat(viewModel.liveDataAppInfo.value).isEqualTo(theText)
    }

    @Test
    fun testEmailClicked() {
        // WHEN
        viewModel.emailClicked()

        // THEN
        assertThat(viewModel.liveEventStartActivity.value).isNotNull
        assertThat(viewModel.liveEventStartActivity.value).isInstanceOf(Intent::class.java)
    }

    @Test
    fun testWebClicked() {
        // WHEN
        viewModel.webClicked()

        // THEN
        assertThat(viewModel.liveEventStartActivity.value).isNotNull
        assertThat(viewModel.liveEventStartActivity.value).isInstanceOf(Intent::class.java)
    }

    @Test
    fun testGitHubClicked() {
        // WHEN
        viewModel.gitHubClicked()

        // THEN
        assertThat(viewModel.liveEventStartActivity.value).isNotNull
        assertThat(viewModel.liveEventStartActivity.value).isInstanceOf(Intent::class.java)
    }

    @Test
    fun testFaqClicked() {
        // WHEN
        viewModel.faqClicked()

        // THEN
        assertThat(viewModel.liveEventStartActivity.value).isNotNull
        assertThat(viewModel.liveEventStartActivity.value).isInstanceOf(Intent::class.java)
    }

    @Test
    fun testLicensesClicked() {
        // WHEN
        viewModel.licensesClicked()

        // THEN
        assertThat(viewModel.liveEventStartActivity.value).isNotNull
        assertThat(viewModel.liveEventStartActivity.value).isInstanceOf(Intent::class.java)
    }

}