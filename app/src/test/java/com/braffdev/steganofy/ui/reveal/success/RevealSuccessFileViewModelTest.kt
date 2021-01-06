package com.braffdev.steganofy.ui.reveal.success

import android.net.Uri
import com.braffdev.steganofy.ui.internal.TestDataFactory
import com.braffdev.steganofy.ui.internal.UnitTest
import com.braffdev.steganofy.ui.reveal.RevealViewModel
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RevealSuccessFileViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: RevealSuccessFileViewModel

    @Mock
    lateinit var parentViewModel: RevealViewModel

    @Test
    fun testInitialize() {
        // GIVEN
        val payload = TestDataFactory.createFilePayload()
        `when`(parentViewModel.outputPayload).thenReturn(payload)
        `when`(fileService.formatFileInfo(anyOrNull(), anyOrNull())).thenReturn("text/plain, 123 bytes")

        // WHEN
        viewModel.initialize()

        // THEN
        assertThat(viewModel.liveDataFileInfoText.value).isNotNull
    }

    @Test
    fun testInitialize_image() {
        // GIVEN
        val payload = TestDataFactory.createImageFilePayload()
        `when`(parentViewModel.outputPayload).thenReturn(payload)
        `when`(parentViewModel.temporaryOutputFileUri).thenReturn(mock())
        `when`(fileService.formatFileInfo(anyOrNull(), anyOrNull())).thenReturn("image/jpeg, 123 bytes")
        `when`(bitmapService.decodeBitmap(anyOrNull())).thenReturn(mock())

        // WHEN
        viewModel.initialize()

        // THEN
        assertThat(viewModel.liveDataFileInfoText.value).isNotNull
        assertThat(viewModel.liveDataImagePreview.value).isNotNull
    }

    @Test
    fun testInitialize_payloadNull() {
        // WHEN
        assertThatThrownBy { viewModel.initialize() }

            // THEN
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("outputPayload must not be null")
    }

    @Test
    fun testInitialize_payloadNotPlainText() {
        // GIVEN
        `when`(parentViewModel.outputPayload).thenReturn(TestDataFactory.createPlainTextPayload())

        // WHEN
        assertThatThrownBy { viewModel.initialize() }

            // THEN
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("outputPayload must be of type FILE")
    }

    @Test
    fun testGetTemporaryFileUri() {
        // GIVEN
        val expectedFileUri: Uri = mock()
        `when`(parentViewModel.temporaryOutputFileUri).thenReturn(expectedFileUri)

        // WHEN
        val temporaryFileUri = viewModel.getTemporaryFileUri()

        // THEN
        assertThat(temporaryFileUri).isEqualTo(expectedFileUri)
    }

}