package com.bity.icp_kotlin_kit.cryptography

internal object Base32 {
    private const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"

    fun encode(data: ByteArray): String {
        val result = StringBuilder()
        var bitBuffer = 0
        var bitCount = 0

        for (b in data) {
            bitBuffer = (bitBuffer shl 8) or (b.toInt() and 0xff)
            bitCount += 8
            while (bitCount >= 5) {
                bitCount -= 5
                result.append(ALPHABET[(bitBuffer shr bitCount) and 0x1f])
            }
        }

        if (bitCount > 0) {
            result.append(ALPHABET[(bitBuffer shl (5 - bitCount)) and 0x1f])
        }

        return result.toString()
    }
}
