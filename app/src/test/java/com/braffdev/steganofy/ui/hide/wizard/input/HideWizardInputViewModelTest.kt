package com.braffdev.steganofy.ui.hide.wizard.input

import com.braffdev.steganofy.R
import com.braffdev.steganofy.ui.hide.wizard.HideWizardViewModel
import com.braffdev.steganofy.ui.internal.UnitTest
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class HideWizardInputViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: HideWizardInputViewModel

    @Mock
    lateinit var parentViewModel: HideWizardViewModel

    @Test
    fun testNextClicked() {
        // GIVEN
        viewModel.payloadPlainTextChanged("JUnit")

        // WHEN
        viewModel.nextClicked()

        // THEN
        verify(parentViewModel).nextClicked()
        verify(parentViewModel).payloadChanged(anyOrNull())
    }

    @Test
    fun testRadioButtonChecked_plainText() {
        // WHEN
        viewModel.radioButtonChecked(R.id.radioButtonPlaintext)

        // THEN
        assertThat(viewModel.liveDataPlainTextShown.value!!).isTrue
        assertThat(viewModel.liveDataSelectFileShown.value!!).isFalse
        assertThat(viewModel.liveDataNextButtonEnabled.value!!).isFalse
    }

    @Test
    fun testRadioButtonChecked_plainText_entered() {
        // GIVEN
        viewModel.payloadPlainTextChanged("JUnit")

        // WHEN
        viewModel.radioButtonChecked(R.id.radioButtonPlaintext)

        // THEN
        assertThat(viewModel.liveDataPlainTextShown.value!!).isTrue
        assertThat(viewModel.liveDataSelectFileShown.value!!).isFalse
        assertThat(viewModel.liveDataNextButtonEnabled.value!!).isTrue
    }

    @Test
    fun testRadioButtonChecked_file() {
        // WHEN
        viewModel.radioButtonChecked(R.id.radioButtonFile)

        // THEN
        assertThat(viewModel.liveDataPlainTextShown.value!!).isFalse
        assertThat(viewModel.liveDataSelectFileShown.value!!).isTrue
        assertThat(viewModel.liveDataNextButtonEnabled.value!!).isFalse
    }

    @Test
    fun testRadioButtonChecked_file_selected() {
        // GIVEN
        viewModel.payloadFileChanged(mock())

        // WHEN
        viewModel.radioButtonChecked(R.id.radioButtonFile)

        // THEN
        assertThat(viewModel.liveDataPlainTextShown.value!!).isFalse
        assertThat(viewModel.liveDataSelectFileShown.value!!).isTrue
        assertThat(viewModel.liveDataNextButtonEnabled.value!!).isTrue
    }
}