package com.braffdev.steganofy.lib.service

import com.braffdev.steganofy.lib.domain.EncryptionAlgorithm
import com.braffdev.steganofy.lib.domain.SteganoData
import org.apache.commons.io.IOUtils
import java.io.InputStream
import java.io.OutputStream

class HideService {

    /**
     * Checks whether a image is large enough to contain the given data
     *
     * @param imageAmountOfBytes the size of the target image
     * @param data the data to hide in the image
     * @return a boolean indicating whether the image is large enough
     */
    fun isImageLargeEnough(imageAmountOfBytes: Int, data: SteganoData): Boolean {
        // 1 = format version
        // 1 = 4 bits type & 4 bits encryption algorithm
        // 4 = payload length
        // n = payload
        val dataLengthToHide = 1 + 1 + 4 + getPayloadBytes(data).size
        return dataLengthToHide * 4 <= imageAmountOfBytes
    }

    /**
     * Hides the given data. If requested, the payload is encrypted.
     *
     * @param inputStream the data that will be used to hide inside
     * @param outputStream the stream to receive the result
     * @param data the data that should be hidden
     */
    fun hide(inputStream: InputStream, outputStream: OutputStream, data: SteganoData) {
        // Format version: 1 byte
        writeBytes(inputStream, outputStream, byteArrayOf(data.version.value))

        // Type: 4 bits
        val type = data.payload.getType().value

        // Encryption algorithm: 4 bits
        val encryptionAlgorithm = data.encryptionAlgorithm.value

        val typeAndEncryption = (type.toInt() shl 4 or encryptionAlgorithm.toInt()).toByte()
        writeBytes(inputStream, outputStream, byteArrayOf(typeAndEncryption))

        // Payload length: 4 bytes
        val payloadBytes = getPayloadBytes(data)
        val payloadLength = payloadBytes.size
        writeBytes(inputStream, outputStream, payloadLength.toByteArray())

        // Payload
        writeBytes(inputStream, outputStream, payloadBytes)

        // Write the remaining bytes as-is to outputStream
        IOUtils.copy(inputStream, outputStream)
    }

    /**
     * Returns the payload as bytes. The data is encrypted if requested.
     *
     * @param data the data to get the payload from
     * @return the payload. Already encrypted if requested
     */
    private fun getPayloadBytes(data: SteganoData): ByteArray {
        val payloadBytes = data.payload.getBytes()
        if (data.encryptionAlgorithm == EncryptionAlgorithm.NONE) {
            return payloadBytes
        }

        return AESEncryptionService().encrypt(payloadBytes, data.encryptionPassword!!)
    }

    /**
     * Encode an array of bytes into another array of bytes
     *
     * @param targetBytes  Array of data that will hold the data
     * @param bytesToHide  Array of data to hide in targetBytes
     * @return Returns the targetBytes containing the bytesToHide
     */
    private fun writeBytes(targetBytes: ByteArray, bytesToHide: ByteArray): ByteArray {
        // Check that the data will fit
        require((bytesToHide.size) * 4 <= targetBytes.size) { "targetBytes not long enough!" }

        // Loop through each byte to hide
        var offsetInBytes = 0
        for (byteToHide in bytesToHide) {

            // Loop through the 8 bits of each byteToHide
            var bit = 0
            while (bit <= 7) {

                // Store the next two bits in the target byte
                var bitToHide = getBit(byteToHide, bit)
                targetBytes[offsetInBytes] = hideBit(targetBytes[offsetInBytes], bitToHide, 6)
                ++bit

                bitToHide = getBit(byteToHide, bit)
                targetBytes[offsetInBytes] = hideBit(targetBytes[offsetInBytes], bitToHide, 7)
                ++bit

                // Use the next target byte for the next two bits
                ++offsetInBytes
            }
        }

        return targetBytes
    }

    /**
     * Reads the required amount of data from inputStream, hides the bytesToHide, and writes the result to outputStream
     *
     * @param inputStream the stream to read the image data from
     * @param outputStream the stream to receive the result
     * @param bytesToHide the bytes to hide
     */
    private fun writeBytes(inputStream: InputStream, outputStream: OutputStream, bytesToHide: ByteArray) {
        val bytes = ByteArray(bytesToHide.size * 4)
        val bytesRead = inputStream.read(bytes, 0, bytes.size)
        require(bytesRead == bytes.size) { "File not long enough!" }

        outputStream.write(writeBytes(bytes, bytesToHide))
    }

    /**
     * Get the desired bit of the byte
     *
     * @param byte the byte to get the desired bit from
     * @param whichBit the bit you want to get in range 0-7 where 0 is the least significant bit
     * @return the bit - either 0 or 1
     */
    private fun getBit(byte: Byte, whichBit: Int): Byte {
        val shiftValue = 7 - whichBit

        // Bitwise AND 1 is used to only get the least significant bit
        return (byte.toInt() shr shiftValue and 1).toByte()
    }

    /**
     * Hides the given bit in the targetByte at the given position
     *
     * @param targetByte the byte the bit should be hidden in
     * @param bitToHide the bit that should be hidden. Either 0 or 1
     * @param whichBit the bit you want to change. These can be the two least significant bits. Either 6 or 7.
     * @return the targetByte containing the bitToHide
     */
    private fun hideBit(targetByte: Byte, bitToHide: Byte, whichBit: Int): Byte {
        require(bitToHide.toInt() == 0 || bitToHide.toInt() == 1) { "bitToHide must be either 0 or 1" }

        val andValue = when (whichBit) {
            7 -> 0xFE    // 1111 1110
            6 -> 0xFD    // 1111 1101
            else -> throw IllegalArgumentException("whichBit must be either 6 or 7")
        }

        // Shift the bit one to the left if bit 6 should be changed. Otherwise bit 7 would be changed always
        val shiftValue = when (whichBit) {
            7 -> 0
            6 -> 1
            else -> throw IllegalArgumentException("whichBit must be between 6 and 7")
        }

        val shiftedBitToHide = bitToHide.toInt() shl shiftValue

        // Changes the specified bit of the byte to be the bit of addition
        return (targetByte.toInt() and andValue or shiftedBitToHide).toByte()
    }
}