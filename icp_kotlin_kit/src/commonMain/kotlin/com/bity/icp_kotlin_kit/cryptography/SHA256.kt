package com.bity.icp_kotlin_kit.cryptography

expect object SHA256 {
    fun sha256(data: ByteArray): ByteArray
    fun doubleSha256(data: ByteArray): ByteArray
}
