package com.bity.icp_kotlin_kit.cryptography

internal expect object DER {

    fun serialise(uncompressedEcPublicKey: ByteArray): ByteArray
}