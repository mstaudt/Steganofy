package com.braffdev.steganofy.ui.hide.wizard.settings

import com.braffdev.steganofy.R
import com.braffdev.steganofy.lib.domain.EncryptionAlgorithm
import com.braffdev.steganofy.ui.hide.wizard.HideWizardViewModel
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HideWizardSettingsViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: HideWizardSettingsViewModel

    @Mock
    lateinit var parentViewModel: HideWizardViewModel

    @Test
    fun testRadioButtonChecked_noEncryption() {
        // WHEN
        viewModel.radioButtonChecked(R.id.radioButtonNoEncryption)

        // THEN
        assertThat(viewModel.liveDataStartButtonEnabled.value).isTrue
        assertThat(viewModel.liveDataShowFileTooSmall.value).isFalse
    }

    @Test
    fun testRadioButtonChecked_AESEncryption() {
        // GIVEN
        `when`(hideService.isImageLargeEnough(any(), any())).thenReturn(true)
        `when`(parentViewModel.bitmap).thenReturn(mock())
        `when`(parentViewModel.payload).thenReturn(mock())
        viewModel.encryptionPasswordChanged("Test".toCharArray())

        // WHEN
        viewModel.radioButtonChecked(R.id.radioButtonAESEncryption)

        // THEN
        assertThat(viewModel.liveDataStartButtonEnabled.value).isTrue
        assertThat(viewModel.liveDataShowFileTooSmall.value).isFalse
    }

    @Test
    fun testRadioButtonChecked_AESEncryption_missingPassword() {
        // GIVEN
        `when`(hideService.isImageLargeEnough(any(), any())).thenReturn(true)
        `when`(parentViewModel.bitmap).thenReturn(mock())
        `when`(parentViewModel.payload).thenReturn(mock())

        // WHEN
        viewModel.radioButtonChecked(R.id.radioButtonAESEncryption)

        // THEN
        assertThat(viewModel.liveDataStartButtonEnabled.value).isFalse
        assertThat(viewModel.liveDataShowFileTooSmall.value).isFalse
    }

    @Test
    fun testRadioButtonChecked_AESEncryption_tooSmall() {
        // GIVEN
        `when`(hideService.isImageLargeEnough(any(), any())).thenReturn(false)
        `when`(parentViewModel.bitmap).thenReturn(mock())
        `when`(parentViewModel.payload).thenReturn(mock())
        viewModel.encryptionPasswordChanged("Test".toCharArray())

        // WHEN
        viewModel.radioButtonChecked(R.id.radioButtonAESEncryption)

        // THEN
        assertThat(viewModel.liveDataStartButtonEnabled.value).isFalse
        assertThat(viewModel.liveDataShowFileTooSmall.value).isTrue
    }

    @Test
    fun testEncryptionPasswordChanged() {
        viewModel.encryptionPasswordChanged("Test".toCharArray())
    }

    @Test
    fun testPreviousClicked() {
        // WHEN
        viewModel.previousClicked()

        // THEN
        verify(parentViewModel).previousClicked()
    }

    @Test
    fun testStart() {
        // GIVEN
        viewModel.radioButtonChecked(R.id.radioButtonNoEncryption)

        // WHEN
        viewModel.start()

        // THEN
        assertThat(viewModel.liveDataStart.value).isNotNull
        verify(parentViewModel).encryptionAlgorithmChanged(EncryptionAlgorithm.NONE)
        verify(parentViewModel, never()).encryptionPasswordChanged(anyOrNull())
        verify(parentViewModel).start()
    }

    @Test
    fun testStart_AES() {
        // GIVEN
        `when`(parentViewModel.bitmap).thenReturn(mock())
        `when`(parentViewModel.payload).thenReturn(mock())
        viewModel.radioButtonChecked(R.id.radioButtonAESEncryption)
        viewModel.encryptionPasswordChanged("Test".toCharArray())

        // WHEN
        viewModel.start()

        // THEN
        assertThat(viewModel.liveDataStart.value).isNotNull
        verify(parentViewModel).encryptionAlgorithmChanged(EncryptionAlgorithm.AES_256)
        verify(parentViewModel).encryptionPasswordChanged("Test".toCharArray())
        verify(parentViewModel).start()
    }

}