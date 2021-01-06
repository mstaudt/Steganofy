package com.braffdev.steganofy.lib.domain

/**
 * The type of the payload
 */
enum class Type(val value: Byte) {

    PLAINTEXT(1), FILE(2);

    companion object {
        fun valueOf(value: Byte): Type {
            for (type in values()) {
                if (type.value == value) {
                    return type
                }
            }

            throw IllegalArgumentException("Unknown type $value")
        }
    }
}