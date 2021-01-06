package com.braffdev.steganofy.lib.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AESEncryptionServiceTest {

    private val encryptionService = AESEncryptionService()

    @Test
    fun testEncryptAndDecrypt() {
        // GIVEN
        val bytes = "This is just a test".toByteArray()
        val password = "Password".toCharArray()

        // WHEN
        val encryptedBytes = encryptionService.encrypt(bytes, password)
        val decryptedBytes = encryptionService.decrypt(encryptedBytes, password)

        // THEN
        assertThat(decryptedBytes).isEqualTo(bytes)
    }

    @Test
    fun testEncrypt_notPlaintext() {
        // GIVEN
        val bytes = "This is just a test".toByteArray()
        val password = "Password".toCharArray()

        // WHEN
        val encryptedBytes = encryptionService.encrypt(bytes, password)

        // THEN
        assertThat(encryptedBytes).isNotEqualTo(bytes)
    }
}