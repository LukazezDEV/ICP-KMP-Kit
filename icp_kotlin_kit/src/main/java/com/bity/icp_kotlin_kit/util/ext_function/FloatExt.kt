package com.bity.icp_kotlin_kit.util.ext_function

import okio.BufferedSource

val Float.bytes: ByteArray
    get() = this.toRawBits().let { bits ->
        byteArrayOf(
            (bits and 0xFF).toByte(),
            ((bits shr 8) and 0xFF).toByte(),
            ((bits shr 16) and 0xFF).toByte(),
            ((bits shr 24) and 0xFF).toByte()
        )
    }

fun Float.Companion.readFrom(source: BufferedSource): Float {
    val bits = source.readIntLe() // Little-endian 32-bit
    return Float.fromBits(bits)
}
