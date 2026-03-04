package com.bity.icp_kotlin_kit.cryptography

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.SHA256

object SHA256 {
    private val hasher = CryptographyProvider.Default.get(SHA256).hasher()
    fun sha256(data: ByteArray): ByteArray =
        hasher.hashBlocking(data)

    fun doubleSha256(data: ByteArray): ByteArray =
        sha256(sha256(data))
}