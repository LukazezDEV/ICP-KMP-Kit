package com.bity.icp_kotlin_kit.util.ext_function

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger
import okio.BufferedSource

// Little-endian = Least significant byte first
val UInt.bytes: ByteArray
    get() = this.toInt().bytes

fun UInt.Companion.readFrom(source: BufferedSource): UInt =
    Int.readFrom(source).toUInt()