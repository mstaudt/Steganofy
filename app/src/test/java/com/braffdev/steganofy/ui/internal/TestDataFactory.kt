package com.braffdev.steganofy.ui.internal

import com.braffdev.steganofy.lib.domain.*
import com.braffdev.steganofy.ui.common.OperationStatistics

class TestDataFactory {

    companion object {

        fun createSteganoInfo(): SteganoInfo {
            return SteganoInfo(payloadType = Type.PLAINTEXT)
        }

        fun createEncryptedSteganoInfo(): SteganoInfo {
            return SteganoInfo(payloadType = Type.FILE, encryptionAlgorithm = EncryptionAlgorithm.AES_256)
        }

        fun createSteganoData(): SteganoData {
            return SteganoData(PlainTextPayload("Test"))
        }

        fun createFileSteganoData(): SteganoData {
            return SteganoData(FilePayload("text/plain", "Test".toByteArray()))
        }

        fun createPlainTextPayload(): PlainTextPayload {
            return PlainTextPayload("Test")
        }

        fun createFilePayload(): FilePayload {
            return FilePayload("text/plain", "Test".toByteArray())
        }

        fun createImageFilePayload(): FilePayload {
            return FilePayload("image/jpeg", "Test".toByteArray())
        }

        fun createStatistics(): OperationStatistics {
            return OperationStatistics(1234, 123)
        }
    }
}