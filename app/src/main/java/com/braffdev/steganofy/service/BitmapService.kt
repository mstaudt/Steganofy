package com.braffdev.steganofy.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class BitmapService(private val applicationContext: Context) {

    /**
     * Reads the bitmap denoted by the given uri
     *
     * @param uri the uri to read the bitmap from. Can be a content provider Uri
     * @return the decoded bitmap
     */
    fun decodeBitmap(uri: Uri): Bitmap {
        val stream = applicationContext.contentResolver.openInputStream(uri)
        return stream.use { BitmapFactory.decodeStream(stream) }
    }

    /**
     * Extracts the pixels of the given bitmap
     *
     * @param bitmap the bitmap to get the pixels from
     * @return the pixels as byte array
     */
    fun getPixelBytes(bitmap: Bitmap): ByteArray {
        val pixelArray = ByteBuffer.allocate(bitmap.allocationByteCount)
        bitmap.copyPixelsToBuffer(pixelArray)
        return pixelArray.array()
    }

    /**
     * Creates a new bitmap with the specified dimensions and pixel information
     *
     * @param pixelBytes the pixels to put into the bitmap
     * @param width the width of the bitmap
     * @param height the height of the bitmap
     * @return a new bitmap with ARGB_8888
     */
    fun createBitmapFromPixelBytes(pixelBytes: ByteArray, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(pixelBytes))

        return bitmap
    }

    /**
     * Compresses the given bitmap to PNG. The result can be used to save the bitmap directly to a file. This operation is lossless.
     *
     * @param bitmap the bitmap to get as PNG
     * @return a byte array containing the bitmap in the format of PNG
     */
    fun compressToPNG(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        stream.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) }
        return stream.toByteArray()
    }
}