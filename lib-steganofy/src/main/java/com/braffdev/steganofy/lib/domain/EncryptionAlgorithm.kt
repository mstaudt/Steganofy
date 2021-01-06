package com.braffdev.steganofy.lib.domain

enum class EncryptionAlgorithm(val value: Byte) {

    NONE(0), AES_256(1);

    companion object {
        fun valueOf(value: Byte): EncryptionAlgorithm {
            for (algorithm in values()) {
                if (algorithm.value == value) {
                    return algorithm
                }
            }

            throw IllegalArgumentException("Unknown encryption algorithm $value")
        }
    }
}