package com.bity.icp_kotlin_kit.cryptography

import java.security.MessageDigest

actual object SHA256 {

    private const val SHA_256_ALGORITHM_NAME = "SHA-256"

    private val messageDigest = MessageDigest.getInstance(SHA_256_ALGORITHM_NAME)

    actual fun sha256(data: ByteArray): ByteArray =
        messageDigest.digest(data)

    actual fun doubleSha256(data: ByteArray): ByteArray =
        sha256(sha256(data))
}