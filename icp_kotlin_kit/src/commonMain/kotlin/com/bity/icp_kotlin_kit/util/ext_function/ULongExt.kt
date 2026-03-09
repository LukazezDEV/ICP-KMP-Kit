package com.bity.icp_kotlin_kit.util.ext_function

import kotlinx.datetime.Instant
import okio.BufferedSource

// ======================= ULong =======================

// Little-endian = least significant byte first
internal val ULong.bytes: ByteArray
    get() = this.toLong().toULongBytes()

private fun Long.toULongBytes(): ByteArray {
    val bytes = ByteArray(Long.SIZE_BYTES)
    var value = this
    for (i in 0 until Long.SIZE_BYTES) {
        bytes[i] = (value and 0xFF).toByte()
        value = value shr 8
    }
    return bytes
}

internal fun ULong.Companion.readFrom(source: BufferedSource): ULong {
    val bytes = source.readByteArray(SIZE_BYTES.toLong())
    var result = 0UL
    for (i in bytes.indices.reversed()) {
        result = (result shl 8) or (bytes[i].toULong() and 0xFFUL)
    }
    return result
}

fun ULong.timestampNanosToInstant(): Instant =
    Instant.fromEpochMilliseconds((this / 1_000_000UL).toLong())