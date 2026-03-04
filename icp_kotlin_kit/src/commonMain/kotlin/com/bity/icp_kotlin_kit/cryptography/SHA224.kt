package com.bity.icp_kotlin_kit.cryptography

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.SHA224

object SHA224 {
    private val hasher = CryptographyProvider.Default.get(SHA224).hasher()

    operator fun invoke(data: ByteArray): ByteArray =
        hasher.hashBlocking(data)
}