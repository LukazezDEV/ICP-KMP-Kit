package com.bity.icp_kotlin_kit.cryptography

import com.bity.icp_kotlin_kit.domain.model.error.ICPCryptographyError
import com.bity.icp_kotlin_kit.util.ext_function.grouped

object ICPCryptography {

    private const val CANONICAL_TEXT_SEPARATOR = "-"

    internal fun encodeCanonicalText(data: ByteArray): String {
        val checksum = CRC32(data)
        val dataWithChecksum = checksum + data
        val base32Encoded = Base32.encode(dataWithChecksum).lowercase()
        return base32Encoded.grouped(CANONICAL_TEXT_SEPARATOR, 5)
    }

    internal fun decodeCanonicalText(text: String): ByteArray {
        val degrouped = text.replace(CANONICAL_TEXT_SEPARATOR, "")
        val decoded = Base32.decode(degrouped)
        val checksum = decoded.take(CRC32.CRC_32_LENGTH).toByteArray()
        val data = decoded.copyOfRange(CRC32.CRC_32_LENGTH, decoded.size)
        val expectedChecksum = CRC32(data)
        require(expectedChecksum.contentEquals(checksum)) {
            throw ICPCryptographyError.ICPCRC32Error.InvalidChecksum()
        }
        return data
    }
}