package com.braffdev.steganofy.ui.hide.wizard

import androidx.lifecycle.Observer
import com.braffdev.steganofy.lib.domain.EncryptionAlgorithm
import com.braffdev.steganofy.ui.hide.HideDataBridge
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HideWizardViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: HideWizardViewModel

    @Mock
    lateinit var hideDataBridge: HideDataBridge

    @Test
    fun testPreviousClicked() {
        // GIVEN
        val observer: Observer<Any> = mock()
        viewModel.liveEventShowCancelDialog.observeForever(observer)

        // WHEN
        viewModel.previousClicked()

        // THEN
        assertThat(viewModel.liveDataCurrentPage.value).isEqualTo(0)
        verify(observer).onChanged(anyOrNull())
    }

    @Test
    fun testPreviousClicked_secondStep() {
        // GIVEN
        testNextClicked()

        // WHEN
        viewModel.previousClicked()

        // THEN
        assertThat(viewModel.liveDataCurrentPage.value).isEqualTo(0)
    }

    @Test
    fun testNextClicked() {
        // WHEN
        viewModel.nextClicked()

        // THEN
        assertThat(viewModel.liveDataCurrentPage.value).isEqualTo(1)
    }

    @Test
    fun testNextClicked_lastStep() {
        // GIVEN
        viewModel.nextClicked()
        viewModel.nextClicked()
        assertThat(viewModel.liveDataCurrentPage.value).isEqualTo(2)

        // WHEN
        viewModel.nextClicked()

        // THEN
        assertThat(viewModel.liveDataCurrentPage.value).isEqualTo(2)
    }

    @Test
    fun testStart_encrypted() {
        // GIVEN
        viewModel.payloadChanged(mock())
        viewModel.imageChanged(mock())
        viewModel.encryptionAlgorithmChanged(EncryptionAlgorithm.AES_256)
        viewModel.encryptionPasswordChanged("Test".toCharArray())

        // WHEN
        viewModel.start()

        // THEN
        verify(hideDataBridge).bitmap = any()
        verify(hideDataBridge).steganoData = any()
    }

    @Test
    fun testStart() {
        // GIVEN
        viewModel.payloadChanged(mock())
        viewModel.imageChanged(mock())
        viewModel.encryptionAlgorithmChanged(EncryptionAlgorithm.NONE)

        // WHEN
        viewModel.start()

        // THEN
        verify(hideDataBridge).bitmap = any()
        verify(hideDataBridge).steganoData = any()
    }
}