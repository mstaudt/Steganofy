package com.braffdev.steganofy.ui.hide

import androidx.lifecycle.Observer
import com.braffdev.steganofy.ui.common.OperationStatus
import com.braffdev.steganofy.ui.internal.TestDataFactory
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HideViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: HideViewModel

    @Mock
    lateinit var hideDataBridge: HideDataBridge

    @Test
    fun testInitialize() {
        // GIVEN
        `when`(hideDataBridge.bitmap).thenReturn(mock())
        `when`(hideDataBridge.steganoData).thenReturn(TestDataFactory.createSteganoData())
        `when`(bitmapService.getPixelBytes(any())).thenReturn("Test".toByteArray())
        `when`(fileService.createTemporaryFile(any(), anyOrNull())).thenReturn(mock())

        // WHEN
        viewModel.initialize()

        // THEN
        assertThat(viewModel.liveDataStatus.value).isEqualTo(OperationStatus.SUCCESS)
        assertThat(viewModel.temporaryOutputFileUri).isNotNull
        assertThat(viewModel.statistics).isNotNull
        assertThat(viewModel.dataSizeInBytes).isNotZero

        verify(hideDataBridge).clear()
        verify(hideService).hide(any(), any(), any())
    }

    @Test
    fun testInitialize_throwingException() {
        // GIVEN
        `when`(hideDataBridge.bitmap).thenReturn(mock())
        `when`(hideDataBridge.steganoData).thenReturn(mock())
        `when`(bitmapService.getPixelBytes(any())).thenReturn("Test".toByteArray())
        `when`(hideService.hide(any(), any(), any())).thenThrow(RuntimeException())

        // WHEN
        viewModel.initialize()

        // THEN
        assertThat(viewModel.liveDataStatus.value).isEqualTo(OperationStatus.ERROR)
        assertThat(viewModel.temporaryOutputFileUri).isNull()
        assertThat(viewModel.statistics).isNull()
        assertThat(viewModel.dataSizeInBytes).isZero()

        verify(hideDataBridge).clear()
    }

    @Test
    fun testInitialize_missingBitmap() {
        // WHEN
        assertThatThrownBy { viewModel.initialize() }

            // THEN
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Bitmap must not be null")
    }

    @Test
    fun testInitialize_missingSteganoData() {
        // GIVEN
        `when`(hideDataBridge.bitmap).thenReturn(mock())

        // WHEN
        assertThatThrownBy { viewModel.initialize() }

            // THEN
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("SteganoData must not be null")
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
        testInitialize_throwingException()

        // WHEN
        viewModel.backPressed()

        // THEN
        assertThat(viewModel.liveEventCancelWarning.value).isNull()
    }

    @Test
    fun testFinish() {
        // GIVEN
        testInitialize()

        // WHEN
        viewModel.finish()

        // THEN
        verify(fileService).deleteTemporaryFile(any())
    }
}