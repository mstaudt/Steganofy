package com.braffdev.steganofy.lib.domain

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class SteganoDataTest {

    @Test
    fun testWithEncryption() {
        SteganoData(payload = PlainTextPayload("Test"),
            encryptionAlgorithm = EncryptionAlgorithm.AES_256,
            encryptionPassword = "Test".toCharArray())
    }

    @Test
    fun testWithEncryptionMissingPassword() {
        // WHEN
        assertThatThrownBy { SteganoData(payload = PlainTextPayload("Test"), encryptionAlgorithm = EncryptionAlgorithm.AES_256) }

            // THEN
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("encryptionPassword must not be null")
    }

    @Test
    fun testWithEncryptionMissingPasswordCopy() {
        // GIVEN
        val data = SteganoData(payload = PlainTextPayload("Test"),
            encryptionAlgorithm = EncryptionAlgorithm.AES_256,
            encryptionPassword = "Test".toCharArray())

        // WHEN
        assertThatThrownBy { data.copy(encryptionPassword = null) }

            // THEN
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("encryptionPassword must not be null")
    }

}