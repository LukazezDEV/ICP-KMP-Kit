package com.bity.icp_kotlin_kit.util.ext_function

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

// ICPBigInteger → bytes (32-bit LE)
fun ICPBigInteger.to32BitsLE(): ByteArray {
    val result = ByteArray(4)
    val intVal = this.toInt()
    for (i in 0 until 4) {
        result[i] = ((intVal shr (i * 8)) and 0xFF).toByte()
    }
    return result
}