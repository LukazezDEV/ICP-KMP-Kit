package com.bity.icp_kotlin_kit.util.ext_function

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger
import okio.BufferedSource

fun UByte.Companion.readFrom(source: BufferedSource): UByte =
    source.readByte().toUByte()