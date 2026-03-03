package com.bity.icp_kotlin_kit.cryptography

import java.security.MessageDigest

actual object SHA224 {

    private const val SHA_224_ALGORITHM_NAME = "SHA-224"
    private val messageDigest =  MessageDigest.getInstance(SHA_224_ALGORITHM_NAME)

    actual operator fun invoke(data: ByteArray): ByteArray =
        messageDigest.digest(data)
}