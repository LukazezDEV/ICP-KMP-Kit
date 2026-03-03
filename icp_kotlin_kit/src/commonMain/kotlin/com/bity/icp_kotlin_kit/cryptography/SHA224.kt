package com.bity.icp_kotlin_kit.cryptography

expect object SHA224 {
    operator fun invoke(data: ByteArray): ByteArray
}