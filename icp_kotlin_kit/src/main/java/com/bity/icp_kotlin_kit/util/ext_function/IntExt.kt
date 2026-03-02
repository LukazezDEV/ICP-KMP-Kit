package com.bity.icp_kotlin_kit.util.ext_function

import kotlinx.datetime.Instant
import okio.BufferedSource
import kotlin.experimental.and

val Int.bytes: ByteArray
    get() {
        val bytes = ByteArray(Int.SIZE_BYTES)
        for (i in 0 until Int.SIZE_BYTES) {
            bytes[i] = ((this shr (i * 8)) and 0xFF).toByte()
        }
        return bytes
    }

fun Int.Companion.readFrom(source: BufferedSource): Int {
    val bytes = source.readByteArray(Int.SIZE_BYTES.toLong())
    var result = 0
    for (i in bytes.indices.reversed()) {
        result = (result shl 8) or (bytes[i].toInt() and 0xFF)
    }
    return result
}