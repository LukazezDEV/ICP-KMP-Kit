package com.bity.icp_kotlin_kit.util.ext_function

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger
import okio.BufferedSource

// Little-endian = Least significant byte first
val UShort.bytes: ByteArray
    get() = this.toShort().bytes

fun UShort.Companion.readFrom(source: BufferedSource): UShort =
    Short.readFrom(source).toUShort()