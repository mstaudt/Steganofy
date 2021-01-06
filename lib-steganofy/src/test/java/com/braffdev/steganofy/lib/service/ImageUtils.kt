package com.braffdev.steganofy.lib.service

import org.apache.commons.io.IOUtils
import java.io.InputStream

class ImageUtils {

    companion object {

        fun getImage(path: String): ByteArray {
            val stream = ImageUtils::class.java.getResourceAsStream(path)
            return stream.use { IOUtils.toByteArray(stream) }
        }

        fun getImageStream(path: String): InputStream {
            return ImageUtils::class.java.getResourceAsStream(path)
        }
    }
}