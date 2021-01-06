package com.braffdev.steganofy.ui.common.message

import com.braffdev.steganofy.R
import com.braffdev.steganofy.ui.internal.UnitTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.InjectMocks

class MessageViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: MessageViewModel

    @Test
    fun testInitialize_info() {
        // GIVEN
        val textRes = 1
        val type = MessageFragment.Type.INFO

        // WHEN
        viewModel.initialize(textRes, type)

        // THEN
        assertThat(viewModel.liveDataTextRes.value).isEqualTo(textRes)
        assertThat(viewModel.liveDataIconRes.value).isEqualTo(R.drawable.baseline_info_24)
        assertThat(viewModel.liveDataBackgroundRes.value).isEqualTo(R.drawable.message_info)
    }

    @Test
    fun testInitialize_error() {
        // GIVEN
        val textRes = 5
        val type = MessageFragment.Type.ERROR

        // WHEN
        viewModel.initialize(textRes, type)

        // THEN
        assertThat(viewModel.liveDataTextRes.value).isEqualTo(textRes)
        assertThat(viewModel.liveDataIconRes.value).isEqualTo(R.drawable.baseline_error_24)
        assertThat(viewModel.liveDataBackgroundRes.value).isEqualTo(R.drawable.message_error)
    }
}