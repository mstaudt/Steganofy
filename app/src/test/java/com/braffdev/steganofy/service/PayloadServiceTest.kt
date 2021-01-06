package com.braffdev.steganofy.service

import com.braffdev.steganofy.lib.domain.FilePayload
import com.braffdev.steganofy.lib.domain.PlainTextPayload
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PayloadServiceTest {

    @InjectMocks
    lateinit var payloadService: PayloadService

    @Mock
    lateinit var fileService: FileService

    @Test
    fun testCreatePayload_plainText() {
        // WHEN
        val payload = payloadService.createPayload("Test", null)

        // THEN
        assertThat(payload).isNotNull
        assertThat(payload).isInstanceOf(PlainTextPayload::class.java)
    }

    @Test
    fun testCreatePayload_file() {
        // GIVEN
        `when`(fileService.getByteArray(any())).thenReturn("Test".toByteArray())
        `when`(fileService.getFileType(any())).thenReturn("text/plain")

        // WHEN
        val payload = payloadService.createPayload(null, mock())

        // THEN
        assertThat(payload).isNotNull
        assertThat(payload).isInstanceOf(FilePayload::class.java)
    }
}