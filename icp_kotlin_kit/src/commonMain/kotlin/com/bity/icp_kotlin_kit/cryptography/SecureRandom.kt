package com.bity.icp_kotlin_kit.cryptography

import dev.whyoleg.cryptography.random.CryptographyRandom

internal fun secureRandomOfLength(byteLength: Int): ByteArray
    = CryptographyRandom.nextBytes(byteLength)