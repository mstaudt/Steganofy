package com.braffdev.steganofy.lib.service

import com.braffdev.steganofy.lib.domain.*
import org.apache.commons.lang3.ArrayUtils
import java.io.InputStream
import java.nio.ByteBuffer

class RevealService {

    /**
     * Tries to reveal some data. Can be used to detect whether there is something hidden.
     *
     * @param inputStream the data to check for hidden data
     * @return the data that was revealed, may be null in case the data could not be read or there is nothing hidden
     */
    fun revealInfo(inputStream: InputStream): SteganoInfo? {
        return try {
            val version = FormatVersion.valueOf(revealByte(inputStream))

            val typeAndEncryption = revealByte(inputStream)
            val payloadType = Type.valueOf((typeAndEncryption.toInt() shr 4 and 0x0F).toByte())
            val encryptionAlgorithm = EncryptionAlgorithm.valueOf((typeAndEncryption.toInt() and 0x0F).toByte())

            SteganoInfo(version, payloadType, encryptionAlgorithm)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Reveals data from the given stream
     *
     * @param inputStream the stream to reveal the data from
     * @param decryptionPassword the password to use for decryption, may be null if the data is not encrypted. This can be checked using the method `revealInfo(InputStream)`
     * @return the revealed data
     */
    fun reveal(inputStream: InputStream, decryptionPassword: CharArray? = null): SteganoData {
        val formatVersion = FormatVersion.valueOf(revealByte(inputStream))
        require(formatVersion == FormatVersion.V1) { "Unsupported format version ${formatVersion.value}" }

        val typeAndEncryption = revealByte(inputStream)
        val type = Type.valueOf((typeAndEncryption.toInt() shr 4 and 0x0F).toByte())
        val encryptionAlgorithm = EncryptionAlgorithm.valueOf((typeAndEncryption.toInt() and 0x0F).toByte())

        val payloadLength = revealInt(inputStream)
        require(payloadLength > 0) { "Illegal payload length: $payloadLength bytes" }
        var payloadBytes = revealBytes(inputStream, payloadLength)

        // Decrypt the payload if necessary
        if (encryptionAlgorithm == EncryptionAlgorithm.AES_256) {
            require(ArrayUtils.isNotEmpty(decryptionPassword)) { "encryptionPassword must not be null" }
            payloadBytes = AESEncryptionService().decrypt(payloadBytes, decryptionPassword!!)
        }

        val payload = when (type) {
            Type.PLAINTEXT -> PlainTextPayload.of(payloadBytes)
            Type.FILE -> FilePayload.of(payloadBytes)
        }

        return SteganoData(payload, formatVersion, encryptionAlgorithm, decryptionPassword)
    }

    /**
     * Reveals the next int
     *
     * @param inputStream the stream to read from
     * @return the revealed int
     */
    private fun revealInt(inputStream: InputStream): Int {
        return ByteBuffer.wrap(revealBytes(inputStream, 4)).int
    }

    /**
     * Reveals the next byte
     *
     * @param inputStream the stream to read from
     * @return the revealed byte
     */
    private fun revealByte(inputStream: InputStream): Byte {
        val bytes = ByteArray(4)
        val bytesRead = inputStream.read(bytes)
        require(bytesRead == bytes.size) { "Unexpected EOF" }

        return revealByte(bytes)
    }

    /**
     * Reveals the next byte
     *
     * @param bytes a 4-byte array to reveal from
     * @return the revealed byte
     */
    private fun revealByte(bytes: ByteArray): Byte {
        require(bytes.size == 4) { "bytes must have a length of 4" }

        val offsetInBytes = 0
        var value = 0
        for (i in offsetInBytes until offsetInBytes + 4) {
            // Get bit 6 of byte
            var bitToRead = bytes[i].toInt() shr 1 and 1
            // Shift the value 1 to the left and assign the bit that was read
            value = value shl 1 or bitToRead

            // Get bit 7 of byte
            bitToRead = bytes[i].toInt() and 1
            // Shift the value 1 to the left and assign the bit that was read
            value = value shl 1 or bitToRead
        }

        return value.toByte()
    }


    /**
     * Reveals multiple bytes
     *
     * @param inputStream the stream to read from
     * @param lengthInBytes the amount of bytes to reveal
     * @return the revealed bytes
     */
    private fun revealBytes(inputStream: InputStream, lengthInBytes: Int): ByteArray {
        val array = ByteArray(lengthInBytes)

        for (i in array.indices) {
            array[i] = revealByte(inputStream)
        }

        return array
    }
}