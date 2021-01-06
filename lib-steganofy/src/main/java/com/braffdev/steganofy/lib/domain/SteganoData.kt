package com.braffdev.steganofy.lib.domain

data class SteganoData(
    val payload: Payload,
    val version: FormatVersion = FormatVersion.V1,
    val encryptionAlgorithm: EncryptionAlgorithm = EncryptionAlgorithm.NONE,
    val encryptionPassword: CharArray? = null,
) {

    init {
        if (encryptionAlgorithm != EncryptionAlgorithm.NONE) {
            require(encryptionPassword != null) { "encryptionPassword must not be null" }
        }
    }

    /**
     * Returns the estimated length in bytes.
     * Note: The actual length may differ because of encryption
     */
    fun getEstimatedLengthInBytes(): Int {
        return 2 + 1 + 1 + 4 + payload.getLengthInBytes()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SteganoData

        if (version != other.version) return false
        if (payload != other.payload) return false
        if (encryptionAlgorithm != other.encryptionAlgorithm) return false
        if (encryptionPassword != null) {
            if (other.encryptionPassword == null) return false
            if (!encryptionPassword.contentEquals(other.encryptionPassword)) return false
        } else if (other.encryptionPassword != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version.hashCode()
        result = 31 * result + payload.hashCode()
        result = 31 * result + encryptionAlgorithm.hashCode()
        result = 31 * result + (encryptionPassword?.contentHashCode() ?: 0)
        return result
    }
}