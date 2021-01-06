package com.braffdev.steganofy.lib.domain

data class PlainTextPayload(val plaintext: String) : Payload {
    override fun getType(): Type {
        return Type.PLAINTEXT
    }

    override fun getBytes(): ByteArray {
        return plaintext.toByteArray()
    }

    override fun getLengthInBytes(): Int {
        return plaintext.toByteArray().size
    }

    companion object {

        fun of(bytes: ByteArray): PlainTextPayload {
            return PlainTextPayload(String(bytes))
        }
    }
}