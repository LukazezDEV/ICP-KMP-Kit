package com.bity.icp_kotlin_kit

internal expect object RustBindings {
    fun blsVerify(
        autograph: ByteArray,
        message: ByteArray,
        key: ByteArray
    ): Int
}