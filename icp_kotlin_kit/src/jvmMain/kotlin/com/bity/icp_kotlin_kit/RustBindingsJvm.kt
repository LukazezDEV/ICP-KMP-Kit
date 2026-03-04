package com.bity.icp_kotlin_kit

internal actual object RustBindings {
    init {
        System.loadLibrary("bls12381")
        require(blsInstantiate() == 1)
    }

    private external fun blsInstantiate(): Int

    actual external fun blsVerify(
        autograph: ByteArray,
        message: ByteArray,
        key: ByteArray
    ): Int
}
