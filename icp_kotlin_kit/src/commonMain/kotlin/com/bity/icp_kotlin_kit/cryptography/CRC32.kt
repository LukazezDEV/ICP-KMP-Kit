package com.bity.icp_kotlin_kit.cryptography

expect object CRC32 {
    val CRC_32_LENGTH : Int

    operator fun invoke(data: ByteArray): ByteArray
}