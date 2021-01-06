package com.braffdev.steganofy.lib.service

import com.braffdev.steganofy.lib.domain.PlainTextPayload
import com.braffdev.steganofy.lib.domain.SteganoData
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.InjectMocks

class HideServiceTest {

    @InjectMocks
    private val hideService = HideService()

    @Test
    fun testIsImageLargeEnough() {
        // GIVEN
        val imageAmountOfBytes = 1024
        val data = SteganoData(PlainTextPayload("JUnit test"))

        // WHEN
        val imageLargeEnough = hideService.isImageLargeEnough(imageAmountOfBytes, data)

        // THEN
        assertThat(imageLargeEnough).isTrue
    }

    @Test
    fun testIsImageLargeEnoughTooSmall() {
        // GIVEN
        val imageAmountOfBytes = 48
        val data = SteganoData(PlainTextPayload("JUnit test"))

        // WHEN
        val imageLargeEnough = hideService.isImageLargeEnough(imageAmountOfBytes, data)

        // THEN
        assertThat(imageLargeEnough).isFalse
    }
}