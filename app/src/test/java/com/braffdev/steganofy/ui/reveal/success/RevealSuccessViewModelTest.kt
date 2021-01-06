package com.braffdev.steganofy.ui.reveal.success

import com.braffdev.steganofy.lib.domain.Type
import com.braffdev.steganofy.ui.internal.TestDataFactory
import com.braffdev.steganofy.ui.internal.UnitTest
import com.braffdev.steganofy.ui.reveal.RevealViewModel
import com.nhaarman.mockitokotlin2.any
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class RevealSuccessViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: RevealSuccessViewModel

    @Mock
    lateinit var parentViewModel: RevealViewModel

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
        assertThat(viewModel.liveDataStatisticsRevealTime.value).isNotNull
        assertThat(viewModel.liveDataStatisticsTotalTime.value).isNotNull
    }

    @Test
    fun testGetPayloadType_plainText() {
        // GIVEN
        `when`(parentViewModel.outputPayload).thenReturn(TestDataFactory.createPlainTextPayload())

        // WHEN
        val type = viewModel.getPayloadType()

        // THEN
        assertThat(type).isEqualTo(Type.PLAINTEXT)
    }

    @Test
    fun testGetPayloadType_file() {
        // GIVEN
        `when`(parentViewModel.outputPayload).thenReturn(TestDataFactory.createFilePayload())

        // WHEN
        val type = viewModel.getPayloadType()

        // THEN
        assertThat(type).isEqualTo(Type.FILE)
    }
}