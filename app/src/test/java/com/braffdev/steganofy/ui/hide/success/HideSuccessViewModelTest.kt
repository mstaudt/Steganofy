package com.braffdev.steganofy.ui.hide.success

import androidx.lifecycle.Observer
import com.braffdev.steganofy.ui.hide.HideViewModel
import com.braffdev.steganofy.ui.internal.TestDataFactory
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class HideSuccessViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: HideSuccessViewModel

    @Mock
    lateinit var parentViewModel: HideViewModel

    @Test
    fun testInitialize() {
        // GIVEN
        `when`(applicationContext.getString(any(), any())).thenReturn("Something")
        `when`(parentViewModel.statistics).thenReturn(TestDataFactory.createStatistics())

        // WHEN
        viewModel.initialize()

        // THEN
        assertThat(viewModel.liveDataStatisticsBytes.value).isNotNull
        assertThat(viewModel.liveDataStatisticsImageProcessing.value).isNotNull
        assertThat(viewModel.liveDataStatisticsHideTime.value).isNotNull
        assertThat(viewModel.liveDataStatisticsTotalTime.value).isNotNull
    }

    @Test
    fun testGetTemporaryFileUri() {
        // GIVEN
        `when`(parentViewModel.temporaryOutputFileUri).thenReturn(mock())

        // WHEN
        val temporaryFileUri = viewModel.getTemporaryFileUri()

        // THEN
        assertThat(temporaryFileUri).isNotNull
    }

    @Test
    fun testImageSaved() {
        // GIVEN
        val observer: Observer<Any> = mock()
        viewModel.liveEventFinish.observeForever(observer)

        // WHEN
        viewModel.imageSaved()

        // THEN
        verify(observer).onChanged(anyOrNull())
    }
}