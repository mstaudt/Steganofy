package com.braffdev.steganofy.ui.reveal.wizard.info

import com.braffdev.steganofy.R
import com.braffdev.steganofy.ui.internal.TestDataFactory
import com.braffdev.steganofy.ui.internal.UnitTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.InjectMocks

class SteganoInfoViewModelTest : UnitTest() {

    @InjectMocks
    lateinit var viewModel: SteganoInfoViewModel

    @Test
    fun testInitialize() {
        // GIVEN
        val steganoInfo = TestDataFactory.createSteganoInfo()

        // WHEN
        viewModel.initialize(steganoInfo)

        // THEN
        assertThat(viewModel.liveDataTextRes.value).isEqualTo(R.string.reveal_info)
        assertThat(viewModel.liveDataVersion.value).isEqualTo(steganoInfo.version.toString())
        assertThat(viewModel.liveDataTypeTextRes.value).isEqualTo(R.string.plaintext)
        assertThat(viewModel.liveDataEncryptionTextRes.value).isEqualTo(R.string.encryption_none)
    }

    @Test
    fun testInitialize_encrypted() {
        // GIVEN
        val steganoInfo = TestDataFactory.createEncryptedSteganoInfo()

        // WHEN
        viewModel.initialize(steganoInfo)

        // THEN
        assertThat(viewModel.liveDataTextRes.value).isEqualTo(R.string.reveal_info_encrypted)
        assertThat(viewModel.liveDataVersion.value).isEqualTo(steganoInfo.version.toString())
        assertThat(viewModel.liveDataTypeTextRes.value).isEqualTo(R.string.file)
        assertThat(viewModel.liveDataEncryptionTextRes.value).isEqualTo(R.string.encryption_aes)
    }
}