package com.braffdev.steganofy.lib.domain

enum class FormatVersion(val value: Byte) {

    V1(1);

    companion object {
        fun valueOf(value: Byte): FormatVersion {
            for (version in values()) {
                if (version.value == value) {
                    return version
                }
            }

            throw IllegalArgumentException("Unknown version $value")
        }
    }
}