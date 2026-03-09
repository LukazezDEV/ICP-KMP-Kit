package com.bity.icp_kotlin_kit.util.ext_function

import okio.BufferedSource

val Double.bytes: ByteArray
    get() {
        val bits = this.toBits()
        val bytes = ByteArray(Long.SIZE_BYTES)
        for (i in 0 until Long.SIZE_BYTES) {
            bytes[i] = ((bits shr (i * 8)) and 0xFF).toByte()
        }
        return bytes
    }

fun Double.Companion.readFrom(source: BufferedSource): Double {
    val bytes = source.readByteArray(Long.SIZE_BYTES.toLong())
    var bits = 0L
    for (i in bytes.indices.reversed()) {
        bits = (bits shl 8) or (bytes[i].toLong() and 0xFF)
    }
    return Double.fromBits(bits)
}