package com.bity.icp_kotlin_kit.util.ext_function

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

/**
 * Convert an ICPBigInteger to a 32‑byte big‑endian representation.
 */
fun ICPBigInteger.to32Bits(): ByteArray {
    val full = this.toByteArray()

    return when {
        full.size == 32 -> full
        full.size > 32 -> full.copyOfRange(full.size - 32, full.size)
        else -> ByteArray(32 - full.size) + full
    }
}
