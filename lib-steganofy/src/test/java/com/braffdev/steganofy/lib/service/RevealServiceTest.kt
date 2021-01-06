package com.braffdev.steganofy.lib.service

import com.braffdev.steganofy.lib.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class RevealServiceTest {

    private val revealService = RevealService()
    private val hideService = HideService()

    @Test
    fun testRevealInfo_plainText() {
        // GIVEN
        val inputStream = ImageUtils.getImageStream("/image_100x100.jpg")
        val outputStream = ByteArrayOutputStream()
        val data = SteganoData(PlainTextPayload("JUnit test"))

        inputStream.use { outputStream.use { hideService.hide(inputStream, outputStream, data) } }
        val inputStreamWithData = ByteArrayInputStream(outputStream.toByteArray())

        // WHEN
        val steganoInfo = inputStreamWithData.use { revealService.revealInfo(inputStreamWithData) }

        // THEN
        assertThat(steganoInfo).isNotNull
        assertThat(steganoInfo!!.version).isEqualTo(FormatVersion.V1)
        assertThat(steganoInfo.payloadType).isEqualTo(Type.PLAINTEXT)
        assertThat(steganoInfo.encryptionAlgorithm).isEqualTo(EncryptionAlgorithm.NONE)
    }

    @Test
    fun testRevealInfo_plainText_encrypted() {
        // GIVEN
        val inputStream = ImageUtils.getImageStream("/image_100x100.jpg")
        val outputStream = ByteArrayOutputStream()
        val data = SteganoData(
            payload = PlainTextPayload("JUnit test"),
            encryptionAlgorithm = EncryptionAlgorithm.AES_256,
            encryptionPassword = "JUnit".toCharArray())

        inputStream.use { outputStream.use { hideService.hide(inputStream, outputStream, data) } }
        val inputStreamWithData = ByteArrayInputStream(outputStream.toByteArray())

        // WHEN
        val steganoInfo = inputStreamWithData.use { revealService.revealInfo(inputStreamWithData) }

        // THEN
        assertThat(steganoInfo).isNotNull
        assertThat(steganoInfo!!.version).isEqualTo(FormatVersion.V1)
        assertThat(steganoInfo.payloadType).isEqualTo(Type.PLAINTEXT)
        assertThat(steganoInfo.encryptionAlgorithm).isEqualTo(EncryptionAlgorithm.AES_256)
    }

    @Test
    fun testRevealInfo_file() {
        // GIVEN
        val inputStream = ImageUtils.getImageStream("/image_400x200.jpg")
        val outputStream = ByteArrayOutputStream()
        val data = SteganoData(payload = FilePayload("image/jpeg", ImageUtils.getImage("/image_50x50.jpg")))

        inputStream.use { outputStream.use { hideService.hide(inputStream, outputStream, data) } }
        val inputStreamWithData = ByteArrayInputStream(outputStream.toByteArray())

        // WHEN
        val steganoInfo = inputStreamWithData.use { revealService.revealInfo(inputStreamWithData) }

        // THEN
        assertThat(steganoInfo).isNotNull
        assertThat(steganoInfo!!.version).isEqualTo(FormatVersion.V1)
        assertThat(steganoInfo.payloadType).isEqualTo(Type.FILE)
        assertThat(steganoInfo.encryptionAlgorithm).isEqualTo(EncryptionAlgorithm.NONE)
    }

    @Test
    fun testRevealInfoMalformed() {
        // GIVEN
        val inputStream = ByteArrayInputStream("This is just a test".toByteArray())

        // WHEN
        val steganoInfo = inputStream.use { revealService.revealInfo(inputStream) }

        // THEN
        assertThat(steganoInfo).isNull()
    }

}