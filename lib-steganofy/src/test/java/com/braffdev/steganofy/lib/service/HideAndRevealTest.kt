package com.braffdev.steganofy.lib.service

import com.braffdev.steganofy.lib.domain.EncryptionAlgorithm
import com.braffdev.steganofy.lib.domain.FilePayload
import com.braffdev.steganofy.lib.domain.PlainTextPayload
import com.braffdev.steganofy.lib.domain.SteganoData
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class HideAndRevealTest {

    private val hideService = HideService()
    private val revealService = RevealService()

    @Test
    fun testHideAndReveal_plainText() {
        // GIVEN
        val inputStream = ImageUtils.getImageStream("/image_100x100.jpg")
        val outputStream = ByteArrayOutputStream()
        val data = SteganoData(PlainTextPayload("JUnit test"))

        // WHEN
        inputStream.use { outputStream.use { hideService.hide(inputStream, outputStream, data) } }
        val inputStreamWithData = ByteArrayInputStream(outputStream.toByteArray())
        val revealedData = inputStreamWithData.use { revealService.reveal(inputStreamWithData) }

        // THEN
        assertThat(revealedData).isEqualTo(data)
    }

    @Test
    fun testHideAndReveal_plainTextEncrypted() {
        // GIVEN
        val inputStream = ImageUtils.getImageStream("/image_100x100.jpg")
        val outputStream = ByteArrayOutputStream()
        val password = "Test".toCharArray()
        val data = SteganoData(PlainTextPayload("JUnit test"), encryptionAlgorithm = EncryptionAlgorithm.AES_256, encryptionPassword = password)

        // WHEN
        inputStream.use { outputStream.use { hideService.hide(inputStream, outputStream, data) } }
        val inputStreamWithData = ByteArrayInputStream(outputStream.toByteArray())
        val revealedData = inputStreamWithData.use { revealService.reveal(inputStreamWithData, password) }

        // THEN
        assertThat(revealedData).isEqualTo(data)
    }

    @Test
    fun testHideAndReveal_file() {
        // GIVEN
        val inputStream = ImageUtils.getImageStream("/image_400x200.jpg")
        val outputStream = ByteArrayOutputStream()
        val data = SteganoData(payload = FilePayload("image/jpeg", ImageUtils.getImage("/image_50x50.jpg")))

        // WHEN
        inputStream.use { outputStream.use { hideService.hide(inputStream, outputStream, data) } }
        val inputStreamWithData = ByteArrayInputStream(outputStream.toByteArray())
        val revealedData = inputStreamWithData.use { revealService.reveal(inputStreamWithData) }

        // THEN
        assertThat((revealedData.payload as FilePayload).mimeType).isEqualTo((data.payload as FilePayload).mimeType)
        assertThat(revealedData).isEqualTo(data)
    }

    @Test
    fun testHideAndReveal_fileEncrypted() {
        // GIVEN
        val inputStream = ImageUtils.getImageStream("/image_400x200.jpg")
        val outputStream = ByteArrayOutputStream()
        val password = "Test".toCharArray()
        val data = SteganoData(payload = FilePayload("image/jpeg", ImageUtils.getImage("/image_50x50.jpg")),
            encryptionAlgorithm = EncryptionAlgorithm.AES_256,
            encryptionPassword = password)

        // WHEN
        inputStream.use { outputStream.use { hideService.hide(inputStream, outputStream, data) } }
        val inputStreamWithData = ByteArrayInputStream(outputStream.toByteArray())
        val revealedData = inputStreamWithData.use { revealService.reveal(inputStreamWithData, password) }

        // THEN
        assertThat((revealedData.payload as FilePayload).mimeType).isEqualTo((data.payload as FilePayload).mimeType)
        assertThat(revealedData).isEqualTo(data)
    }
}