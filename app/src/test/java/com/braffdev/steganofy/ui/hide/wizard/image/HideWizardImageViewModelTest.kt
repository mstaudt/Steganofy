package com.braffdev.steganofy.ui.hide.wizard.image

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
class HideWizardImageViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: HideWizardImageViewModel

    @Mock
    lateinit var parentViewModel: HideWizardViewModel

    @Test
    fun testImageUriChanged() {
        // GIVEN
        `when`(parentViewModel.bitmap).thenReturn(mock())
        `when`(parentViewModel.payload).thenReturn(mock())
        `when`(hideService.isImageLargeEnough(anyOrNull(), anyOrNull())).thenReturn(true)

        // WHEN
        viewModel.imageUriChanged(mock())

        // THEN
        assertThat(viewModel.liveDataShowImageTooLarge.value).isFalse
        assertThat(viewModel.liveDataShowImageTooSmall.value).isFalse
        assertThat(viewModel.liveDataNextButtonEnabled.value).isTrue
        verify(parentViewModel).imageChanged(anyOrNull())
    }

    @Test
    fun testImageUriChanged_tooSmall() {
        // GIVEN
        `when`(parentViewModel.bitmap).thenReturn(mock())
        `when`(parentViewModel.payload).thenReturn(mock())
        `when`(hideService.isImageLargeEnough(anyOrNull(), anyOrNull())).thenReturn(false)

        // WHEN
        viewModel.imageUriChanged(mock())

        // THEN
        assertThat(viewModel.liveDataShowImageTooLarge.value).isFalse
        assertThat(viewModel.liveDataShowImageTooSmall.value).isTrue
        assertThat(viewModel.liveDataNextButtonEnabled.value).isFalse
        verify(parentViewModel).imageChanged(anyOrNull())
    }

    @Test
    fun testImageUriChanged_tooLarge() {
        // GIVEN
        `when`(fileService.getFileSize(any())).thenReturn(30 * 1000 * 1000) // 30 MB

        // WHEN
        viewModel.imageUriChanged(mock())

        // THEN
        assertThat(viewModel.liveDataShowImageTooLarge.value).isTrue
        assertThat(viewModel.liveDataShowImageTooSmall.value).isFalse
        assertThat(viewModel.liveDataNextButtonEnabled.value).isFalse
        verify(parentViewModel, never()).imageChanged(anyOrNull())
    }

    @Test
    fun testResume() {
        // GIVEN
        testImageUriChanged()

        // WHEN
        viewModel.resume()

        // THEN
        assertThat(viewModel.liveDataShowImageTooLarge.value).isFalse
        assertThat(viewModel.liveDataShowImageTooSmall.value).isFalse
        assertThat(viewModel.liveDataNextButtonEnabled.value).isTrue
    }

    @Test
    fun testResume_tooSmall() {
        // GIVEN
        testImageUriChanged_tooSmall()

        // WHEN
        viewModel.resume()

        // THEN
        assertThat(viewModel.liveDataShowImageTooLarge.value).isFalse
        assertThat(viewModel.liveDataShowImageTooSmall.value).isTrue
        assertThat(viewModel.liveDataNextButtonEnabled.value).isFalse
    }

    @Test
    fun testNextClicked() {
        // WHEN
        viewModel.nextClicked()

        // THEN
        verify(parentViewModel).nextClicked()
    }

    @Test
    fun testPreviousClicked() {
        // WHEN
        viewModel.previousClicked()

        // THEN
        verify(parentViewModel).previousClicked()
    }
}