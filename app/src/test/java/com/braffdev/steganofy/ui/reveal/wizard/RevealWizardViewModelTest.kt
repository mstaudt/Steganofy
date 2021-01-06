package com.braffdev.steganofy.ui.reveal.wizard

import com.braffdev.steganofy.ui.internal.TestDataFactory
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`

class RevealWizardViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: RevealWizardViewModel

    @Before
    fun setUp() {
        `when`(bitmapService.getPixelBytes(any())).thenReturn("JUnit".toByteArray())
        `when`(bitmapService.decodeBitmap(any())).thenReturn(mock())
    }

    @Test
    fun testStartClicked() {
        // WHEN
        viewModel.startClicked()

        // THEN
        assertThat(viewModel.liveDataStart.value).isNull()
    }

    @Test
    fun testStartClicked_imageSelected() {
        // GIVEN
        viewModel.imageUriChanged(mock())

        // WHEN
        viewModel.startClicked()

        // THEN
        assertThat(viewModel.liveDataStart.value).isNotNull
    }

    @Test
    fun testImageUriChanged_nothingHidden() {
        // GIVEN
        `when`(revealService.revealInfo(any())).thenReturn(null)

        // WHEN
        viewModel.imageUriChanged(mock())

        // THEN
        assertThat(viewModel.liveDataInputState.value).isEqualTo(RevealWizardState.INPUT_INVALID)
        assertThat(viewModel.liveDataStartEnabled.value).isFalse
        assertThat(viewModel.liveDataDecryptionPasswordShown.value).isFalse
    }

    @Test
    fun testImageUriChanged() {
        // GIVEN
        `when`(revealService.revealInfo(any())).thenReturn(TestDataFactory.createSteganoInfo())

        // WHEN
        viewModel.imageUriChanged(mock())

        // THEN
        assertThat(viewModel.liveDataInputState.value).isEqualTo(RevealWizardState.INPUT_VALID)
        assertThat(viewModel.liveDataStartEnabled.value).isTrue
        assertThat(viewModel.liveDataDecryptionPasswordShown.value).isFalse
    }

    @Test
    fun testImageUriChanged_encrypted() {
        // GIVEN
        `when`(revealService.revealInfo(any())).thenReturn(TestDataFactory.createEncryptedSteganoInfo())

        // WHEN
        viewModel.imageUriChanged(mock())

        // THEN
        assertThat(viewModel.liveDataInputState.value).isEqualTo(RevealWizardState.INPUT_VALID)
        assertThat(viewModel.liveDataStartEnabled.value).isFalse
        assertThat(viewModel.liveDataDecryptionPasswordShown.value).isTrue
    }

    @Test
    fun testDecryptionPasswordChanged() {
        // GIVEN
        `when`(revealService.revealInfo(any())).thenReturn(TestDataFactory.createEncryptedSteganoInfo())
        viewModel.imageUriChanged(mock())

        // WHEN
        viewModel.decryptionPasswordChanged("smth".toCharArray())

        // THEN
        assertThat(viewModel.liveDataInputState.value).isEqualTo(RevealWizardState.INPUT_VALID)
        assertThat(viewModel.liveDataStartEnabled.value).isTrue
        assertThat(viewModel.liveDataDecryptionPasswordShown.value).isTrue
    }
}