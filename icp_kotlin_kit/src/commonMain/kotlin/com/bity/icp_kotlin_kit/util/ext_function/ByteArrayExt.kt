package com.bity.icp_kotlin_kit.util.ext_function

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

fun ByteArray.toHexString(): String =
    joinToString("") { "%02x".format(it) }

// Little-endian conversion
fun ByteArray.toShort(): Short =
    ((this[1].toInt() and 0xFF) shl 8 or
            (this[0].toInt() and 0xFF)).toShort()

fun ByteArray.toInt(): Int {
    var result = 0
    for (b in this) {
        result = (result shl 8) or (b.toInt() and 0xFF)
    }
    return result
}

fun ByteArray?.toLong(): Long =
    if (this == null || this.isEmpty()) 0L
    else ICPBigInteger.fromSignMagnitude(1, this).toLong()