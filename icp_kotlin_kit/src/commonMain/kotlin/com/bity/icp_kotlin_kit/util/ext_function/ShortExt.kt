package com.bity.icp_kotlin_kit.util.ext_function

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger
import okio.BufferedSource

val Short.bytes: ByteArray
    get() {
        val bytes = ByteArray(Short.SIZE_BYTES)
        var value = this.toInt()
        for (i in 0 until Short.SIZE_BYTES) {
            bytes[i] = (value and 0xFF).toByte()
            value = value shr 8
        }
        return bytes
    }

fun Short.Companion.readFrom(source: BufferedSource): Short {
    val bytes = source.readByteArray(Short.SIZE_BYTES.toLong())
    var result = 0
    for (i in bytes.indices.reversed()) {
        result = (result shl 8) or (bytes[i].toInt() and 0xFF)
    }
    return result.toShort()
}