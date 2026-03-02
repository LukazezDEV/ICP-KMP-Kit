package com.bity.icp_kotlin_kit.util.ext_function

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

fun ByteArray.toHexString(): String =
    joinToString("") { "%02x".format(it) }

// Little-endian conversion
fun ByteArray.toShort(): Short =
    ((this[1].toInt() and 0xFF) shl 8 or
            (this[0].toInt() and 0xFF)).toShort()

fun ByteArray.toInt(): Int =
    (this[3].toInt() and 0xFF shl 24) or
            (this[2].toInt() and 0xFF shl 16) or
            (this[1].toInt() and 0xFF shl 8) or
            (this[0].toInt() and 0xFF)

fun ByteArray?.toLong(): Long =
    if (this == null || this.isEmpty()) 0L
    else ICPBigInteger.fromSignMagnitude(1, this).toLong()