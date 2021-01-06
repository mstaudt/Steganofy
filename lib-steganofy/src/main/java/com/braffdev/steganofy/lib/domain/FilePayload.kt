package com.braffdev.steganofy.lib.domain

import com.braffdev.steganofy.lib.service.toByteArray
import java.nio.ByteBuffer

data class FilePayload(val mimeType: String, val file: ByteArray) : Payload {
    override fun getType(): Type {
        return Type.FILE
    }

    override fun getBytes(): ByteArray {
        val mimeTypeInBytes = mimeType.toByteArray()
        val payloadBytes = ByteBuffer.allocate(getLengthInBytes())

        // MimeType length: 4 bytes
        payloadBytes.put(mimeTypeInBytes.size.toByteArray())

        // MimeType: n bytes
        payloadBytes.put(mimeTypeInBytes)

        // File content
        payloadBytes.put(file)
        return payloadBytes.array()
    }

    override fun getLengthInBytes(): Int {
        // 4 bytes mime type length
        return 4 + mimeType.toByteArray().size + file.size
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilePayload

        if (mimeType != other.mimeType) return false
        if (!file.contentEquals(other.file)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mimeType.hashCode()
        result = 31 * result + file.contentHashCode()
        return result
    }

    companion object {

        fun of(bytes: ByteArray): FilePayload {
            val mimeTypeLength = ByteBuffer.wrap(arrayOf(bytes[0], bytes[1], bytes[2], bytes[3]).toByteArray()).int
            val mimeTypeBytes = bytes.copyOfRange(4, mimeTypeLength + 4)
            val contentBytes = bytes.copyOfRange(mimeTypeLength + 4, bytes.size)
            return FilePayload(String(mimeTypeBytes), contentBytes)
        }
    }


}