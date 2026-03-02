package com.bity.icp_kotlin_kit.util.ext_function

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger
import okio.BufferedSource

val Long.bytes: ByteArray
    get() {
        val bytes = ByteArray(Long.SIZE_BYTES)
        var value = this
        for (i in 0 until Long.SIZE_BYTES) {
            bytes[i] = (value and 0xFF).toByte()
            value = value shr 8
        }
        return bytes
    }

fun Long.Companion.readFrom(source: BufferedSource): Long {
    val bytes = source.readByteArray(Long.SIZE_BYTES.toLong())
    var result = 0L
    for (i in bytes.indices.reversed()) {
        result = (result shl 8) or (bytes[i].toLong() and 0xFF)
    }
    return result
}

internal fun Long.toICPTimestamp(): ULong =
    this.toULong() * 1_000_000UL