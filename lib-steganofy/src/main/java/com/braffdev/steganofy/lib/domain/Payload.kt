package com.braffdev.steganofy.lib.domain

interface Payload {

    fun getType(): Type

    fun getLengthInBytes(): Int

    fun getBytes(): ByteArray
}

