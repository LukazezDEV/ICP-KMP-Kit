package com.bity.icp_kotlin_kit.cryptography

import okio.Buffer

object CRC32 {

    const val CRC_32_LENGTH = 4
    private const val POLY = 0xEDB88320.toInt()

    operator fun invoke(data: ByteArray): ByteArray {
        var crc = -1 // 0xFFFFFFFF

        for (byte in data) {
            crc = crc xor (byte.toInt() and 0xFF)
            repeat(8) {
                crc = if ((crc and 1) != 0) {
                    (crc ushr 1) xor POLY
                } else {
                    crc ushr 1
                }
            }
        }

        val finalizedCrc = crc.inv()

        return Buffer().writeInt(finalizedCrc).readByteArray()
    }
}