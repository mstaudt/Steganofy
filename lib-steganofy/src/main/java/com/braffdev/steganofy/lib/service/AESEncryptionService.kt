package com.braffdev.steganofy.lib.service

import java.nio.ByteBuffer
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class AESEncryptionService {

    /**
     * Encrypts the given data using AES-256. A new salt and initialization vector is generated upon every encryption operation.
     *
     * @param bytes the bytes to encrypt
     * @param password the password to use for encryption
     * @return the cipher text. The cipher text is prefixed with a 12 byte initialization vector and a 16 byte salt
     */
    fun encrypt(bytes: ByteArray, password: CharArray): ByteArray {
        val salt = getRandomNonce(SALT_LENGTH_BYTE)
        val aesKeyFromPassword = getAESKeyFromPassword(password, salt)

        val cipher: Cipher = Cipher.getInstance(ALGORITHM)
        val iv = getRandomNonce(IV_LENGTH_BYTE)
        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, GCMParameterSpec(TAG_LENGTH_BIT, iv))
        val cipherText: ByteArray = cipher.doFinal(bytes)

        // prefix IV and Salt to cipher text
        return ByteBuffer.allocate(iv.size + salt.size + cipherText.size)
            .put(iv).put(salt).put(cipherText).array()
    }

    /**
     * Decrypts the given cipher text using AES-256. The salt and initialization vector are read from the given ciphertext to enable decryption.
     *
     * @param bytes the cipher text to decrypt
     * @param password the password to use for decryption
     * @return the decrypted data
     */
    fun decrypt(bytes: ByteArray, password: CharArray): ByteArray {
        // get back the iv and salt from the cipher text
        val bb = ByteBuffer.wrap(bytes)

        val iv = ByteArray(IV_LENGTH_BYTE)
        bb.get(iv)

        val salt = ByteArray(SALT_LENGTH_BYTE)
        bb.get(salt)

        val cipherText = ByteArray(bb.remaining())
        bb.get(cipherText)

        // get back the aes key from the same password and salt
        val aesKeyFromPassword: SecretKey = getAESKeyFromPassword(password, salt)

        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, GCMParameterSpec(TAG_LENGTH_BIT, iv))
        return cipher.doFinal(cipherText)
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun getAESKeyFromPassword(password: CharArray, salt: ByteArray): SecretKey {
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(password, salt, 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    private fun getRandomNonce(numBytes: Int): ByteArray {
        val nonce = ByteArray(numBytes)
        SecureRandom().nextBytes(nonce)
        return nonce
    }

    companion object {
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val TAG_LENGTH_BIT = 128
        private const val IV_LENGTH_BYTE = 12
        private const val SALT_LENGTH_BYTE = 16
    }
}