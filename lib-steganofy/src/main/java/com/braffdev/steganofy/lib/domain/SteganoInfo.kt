package com.braffdev.steganofy.lib.domain

import java.io.Serializable

data class SteganoInfo(
    val version: FormatVersion = FormatVersion.V1,
    val payloadType: Type,
    val encryptionAlgorithm: EncryptionAlgorithm = EncryptionAlgorithm.NONE,
) : Serializable