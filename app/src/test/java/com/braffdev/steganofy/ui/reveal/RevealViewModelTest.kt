package com.braffdev.steganofy.ui.reveal

import androidx.lifecycle.Observer
import com.braffdev.steganofy.ui.common.OperationStatus
import com.braffdev.steganofy.ui.internal.TestDataFactory
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RevealViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: RevealViewModel

    @Before
    fun setUp() {
        `when`(bitmapService.getPixelBytes(anyOrNull())).thenReturn("JUnit".toByteArray())
    }

    @Test
    fun testInitialize_plainText() {
        // GIVEN
        `when`(revealService.reveal(any(), anyOrNull())).thenReturn(TestDataFactory.createSteganoData())

        // WHEN
        viewModel.initialize(mock(), null)

        // THEN
        assertThat(viewModel.liveDataStatus.value).isEqualTo(OperationStatus.SUCCESS)
        assertThat(viewModel.outputPayload).isNotNull
        assertThat(viewModel.temporaryOutputFileUri).isNull()
        assertThat(viewModel.statistics).isNotNull
        assertThat(viewModel.dataSizeInBytes).isNotZero
    }

    @Test
    fun testInitialize_file() {
        // GIVEN
        `when`(revealService.reveal(any(), anyOrNull())).thenReturn(TestDataFactory.createFileSteganoData())
        `when`(fileService.createTemporaryFile(any(), any())).thenReturn(mock())

        // WHEN
        viewModel.initialize(mock(), null)

        // THEN
        assertThat(viewModel.liveDataStatus.value).isEqualTo(OperationStatus.SUCCESS)
        assertThat(viewModel.outputPayload).isNotNull
        assertThat(viewModel.temporaryOutputFileUri).isNotNull
        assertThat(viewModel.statistics).isNotNull
        assertThat(viewModel.dataSizeInBytes).isNotZero
    }

    @Test
    fun testInitialize_exception() {
        // GIVEN
        `when`(revealService.reveal(any(), anyOrNull())).thenThrow(IllegalArgumentException())

        // WHEN
        viewModel.initialize(mock(), null)

        // THEN
        assertThat(viewModel.liveDataStatus.value).isEqualTo(OperationStatus.ERROR)
        assertThat(viewModel.outputPayload).isNull()
        assertThat(viewModel.temporaryOutputFileUri).isNull()
        assertThat(viewModel.statistics).isNull()
        assertThat(viewModel.dataSizeInBytes).isZero
    }

    @Test
    fun testBackPressed() {
        // GIVEN
        val observer: Observer<Any> = mock()
        viewModel.liveEventCancelWarning.observeForever(observer)

        // WHEN
        viewModel.backPressed()

        // THEN
        verify(observer).onChanged(anyOrNull())
    }

    @Test
    fun testBackPressedError() {
        // GIVEN
        testInitialize_exception()

        // WHEN
        viewModel.backPressed()

        // THEN
        assertThat(viewModel.liveEventCancelWarning.value).isNull()
    }

    @Test
    fun testFinish() {
        // GIVEN
        testInitialize_file()

        // WHEN
        viewModel.finish()

        // THEN
        verify(fileService).deleteTemporaryFile(any())
    }
}