package com.braffdev.steganofy.ui.reveal.success

import android.content.ClipboardManager
import com.braffdev.steganofy.ui.internal.TestDataFactory
import com.braffdev.steganofy.ui.internal.UnitTest
import com.braffdev.steganofy.ui.reveal.RevealViewModel
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
class RevealSuccessPlainTextViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: RevealSuccessPlainTextViewModel

    @Mock
    lateinit var parentViewModel: RevealViewModel

    @Test
    fun testInitialize() {
        // GIVEN
        val payload = TestDataFactory.createPlainTextPayload()
        `when`(parentViewModel.outputPayload).thenReturn(payload)

        // WHEN
        viewModel.initialize()

        // THEN
        assertThat(viewModel.liveDataPlainText.value).isEqualTo(payload.plaintext)
    }

    @Test
    fun testCopyToClipBoardClicked() {
        // GIVEN
        val payload = TestDataFactory.createPlainTextPayload()
        val clipBoardManager: ClipboardManager = mock()
        `when`(parentViewModel.outputPayload).thenReturn(payload)
        `when`(applicationContext.getSystemService(any())).thenReturn(clipBoardManager)

        // WHEN
        viewModel.copyToClipBoardClicked()

        // THEN
        verify(clipBoardManager).setPrimaryClip(anyOrNull())
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
        `when`(parentViewModel.outputPayload).thenReturn(TestDataFactory.createFilePayload())

        // WHEN
        assertThatThrownBy { viewModel.initialize() }

            // THEN
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("outputPayload must be of type PLAINTEXT")
    }

}