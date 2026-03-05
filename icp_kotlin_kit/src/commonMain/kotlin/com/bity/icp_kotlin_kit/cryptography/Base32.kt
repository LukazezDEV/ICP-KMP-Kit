package com.bity.icp_kotlin_kit.cryptography

internal object Base32 {
    private const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
    private val ALPHABET_CHARS = ALPHABET.toCharArray()

    private val DECODE_TABLE = IntArray(256) { -1 }.apply {
        for (i in ALPHABET.indices) {
            val c = ALPHABET[i]
            this[c.code] = i
            this[c.lowercaseChar().code] = i // allow lowercase
        }
    }

    fun encode(data: ByteArray): String {
        if (data.isEmpty()) return ""

        val result = StringBuilder()
        var bitBuffer = 0
        var bitCount = 0

        for (b in data) {
            bitBuffer = (bitBuffer shl 8) or (b.toInt() and 0xff)
            bitCount += 8
            while (bitCount >= 5) {
                bitCount -= 5
                result.append(ALPHABET_CHARS[(bitBuffer shr bitCount) and 0x1f])
            }
        }

        if (bitCount > 0) {
            result.append(ALPHABET_CHARS[(bitBuffer shl (5 - bitCount)) and 0x1f])
        }

        return result.toString()
    }

    fun decode(input: String): ByteArray {
        if (input.isEmpty()) return ByteArray(0)

        // ICP Base32: uppercase, no padding, no whitespace
        val cleaned = input.trim().trimEnd('=')

        var bitBuffer = 0
        var bitCount = 0
        val out = ByteArray(cleaned.length * 5 / 8 + 2)
        var outPos = 0

        for (c in cleaned) {
            val v = DECODE_TABLE[c.code]
            require(v >= 0) { "Invalid Base32 character: '$c'" }

            bitBuffer = (bitBuffer shl 5) or v
            bitCount += 5

            if (bitCount >= 8) {
                bitCount -= 8
                out[outPos++] = ((bitBuffer shr bitCount) and 0xFF).toByte()
                bitBuffer = bitBuffer and ((1 shl bitCount) - 1)
            }
        }

        // RFC 4648: leftover bits must be zero
        if (bitCount in 1..4 && bitBuffer != 0) {
            throw IllegalArgumentException("Invalid Base32 encoding: leftover bits are non-zero")
        }

        return out.copyOf(outPos)
    }
}
