package com.braffdev.steganofy

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.braffdev.steganofy.lib.domain.PlainTextPayload
import com.braffdev.steganofy.lib.domain.SteganoData
import com.braffdev.steganofy.lib.service.HideService
import com.braffdev.steganofy.lib.service.RevealService
import com.braffdev.steganofy.service.BitmapService
import com.braffdev.steganofy.service.FileService
import com.braffdev.steganofy.test.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@RunWith(AndroidJUnit4::class)
class BitmapServiceTest {

    private val bitmapService = BitmapService(InstrumentationRegistry.getInstrumentation().context)
    private val fileService = FileService(InstrumentationRegistry.getInstrumentation().context)

    @Test
    fun testGetPixelBytes_createBitmapFromPixelBytes() {
        // GIVEN
        val targetContext = InstrumentationRegistry.getInstrumentation().context
        val uri = Uri.parse("android.resource://" + targetContext.packageName + "/" + R.raw.image_100x100)

        // WHEN
        val bitmap = bitmapService.decodeBitmap(uri)
        val bitmapPixelsAsBytes = bitmapService.getPixelBytes(bitmap)
        val reconvertedBitmap = bitmapService.createBitmapFromPixelBytes(bitmapPixelsAsBytes, bitmap.width, bitmap.height)

        // THEN
        assertThat(bitmapPixelsAsBytes).isEqualTo(bitmapService.getPixelBytes(reconvertedBitmap))
    }

    @Test
    fun testHideAndReveal() {
        // GIVEN
        val targetContext = InstrumentationRegistry.getInstrumentation().context
        val uri = Uri.parse("android.resource://" + targetContext.packageName + "/" + R.raw.image_100x100)
        val data = SteganoData(PlainTextPayload("Test"))

        // WHEN
        val bitmap = bitmapService.decodeBitmap(uri)
        val bitmapPixelsAsBytes = bitmapService.getPixelBytes(bitmap)
        val inputStream = ByteArrayInputStream(bitmapPixelsAsBytes)
        val outputStream = ByteArrayOutputStream()
        inputStream.use { outputStream.use { HideService().hide(inputStream, outputStream, data) } }
        val inputStreamWithData = ByteArrayInputStream(outputStream.toByteArray())
        val revealed = inputStreamWithData.use { RevealService().reveal(inputStreamWithData) }

        // THEN
        assertThat(revealed).isNotNull
    }

    @Test
    fun testCompressToPNG() {
        // GIVEN
        val context = InstrumentationRegistry.getInstrumentation().context
        val uri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.image_100x100)

        // WHEN
        val bitmap = bitmapService.decodeBitmap(uri)
        val bitmapBytesPNG = bitmapService.compressToPNG(bitmap)

        // THEN
        assertThat(bitmapBytesPNG).isEqualTo(fileService.getByteArray(uri))
    }
}