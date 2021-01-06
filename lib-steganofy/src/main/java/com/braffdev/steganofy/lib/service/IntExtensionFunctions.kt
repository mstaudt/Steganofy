package com.braffdev.steganofy.lib.service

/**
 * Generates proper byte format of an integer
 * @return Returns a byte[4] array converting the supplied integer into bytes
 */
fun Int.toByteArray(): ByteArray {
    val byte3 = (this and -0x1000000 ushr 24).toByte()
    val byte2 = (this and 0x00FF0000 ushr 16).toByte()
    val byte1 = (this and 0x0000FF00 ushr 8).toByte()
    val byte0 = (this and 0x000000FF).toByte()

    return byteArrayOf(byte3, byte2, byte1, byte0)
}