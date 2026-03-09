package com.bity.icp_kotlin_kit.cryptography

import com.bity.icp_kotlin_kit.domain.model.ICPDomainSeparator
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger
import fr.acinq.secp256k1.Secp256k1

object EllipticSign {

    /**
     * secp256k1 curve order (n) as a 32-byte big-endian array.
     * Used for low-S canonicalization.
     * from https://www.secg.org/sec2-v2.pdf
     */
    private val CURVE_ORDER_BYTES = byteArrayOf(
        0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
        0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
        0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
        0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFE.toByte(),
        0xBA.toByte(), 0xAE.toByte(), 0xDC.toByte(), 0xE6.toByte(),
        0xAF.toByte(), 0x48.toByte(), 0xA0.toByte(), 0x3B.toByte(),
        0xBF.toByte(), 0xD2.toByte(), 0x5E.toByte(), 0x8C.toByte(),
        0xD0.toByte(), 0x36.toByte(), 0x41.toByte(), 0x41.toByte()
    )

    /**
     * Signs the given message with domain separation using a 32-byte secp256k1 private key.
     *
     * Returns a 65-byte signature: R(32) || S(32) || recId(1).
     */
    operator fun invoke(
        message: ByteArray,
        domain: String,
        privateKey32: ByteArray
    ): ByteArray {
        require(privateKey32.size == 32) { "Private key must be 32 bytes" }

        val icpDomain = ICPDomainSeparator(domain)
        val domainSeparated = icpDomain.domainSeparatedData(message)

        val hash = SHA256.sha256(domainSeparated)
        require(hash.size == 32) { "SHA-256 hash must be 32 bytes" }

        return signMessage(hash, privateKey32)
    }

    private fun signMessage(
        messageHash32: ByteArray,
        privateKey32: ByteArray
    ): ByteArray {
        // 1. Compact 64-byte signature: R || S
        val rawSig = Secp256k1.sign(messageHash32, privateKey32)

        // 2. Enforce low-S canonicalization
        val sig64 = enforceLowS(rawSig)

        // 3. Compute recovery ID by matching recovered pubkey to actual one
        val actualPubKey = Secp256k1.pubkeyCreate(privateKey32)

        var recId = -1
        for (i in 0..3) {
            val recovered = runCatching {
                Secp256k1.ecdsaRecover(sig64, messageHash32, i)
            }.getOrNull()

            if (recovered != null && recovered.contentEquals(actualPubKey)) {
                recId = i
                break
            }
        }
        require(recId != -1) { "Could not determine recovery id" }

        // 4. Return R(32) || S(32) || recId(1)
        return sig64 + byteArrayOf(recId.toByte())
    }

    /**
     * Ensures S is in the lower half of the curve order.
     * Matches BouncyCastle canonicalization, avoiding malleability.
     */
    private fun enforceLowS(sig64: ByteArray): ByteArray {
        val r = sig64.copyOfRange(0, 32)
        val s = sig64.copyOfRange(32, 64)

        val sBig = ICPBigInteger.fromByteArray(s)
        val n = ICPBigInteger.fromByteArray(CURVE_ORDER_BYTES)
        val halfN = n.shiftRight(1)

        return if (sBig.compareTo(halfN) > 0) {
            val lowS = n.subtract(sBig).toByteArray().padTo32()
            r + lowS
        } else {
            sig64
        }
    }

    /**
     * Pads or trims a byte array to exactly 32 bytes.
     * - If <32: left-pad with zeros
     * - If >32: take the last 32 bytes
     */
    private fun ByteArray.padTo32(): ByteArray =
        when {
            size == 32 -> this
            size < 32 -> ByteArray(32 - size) + this
            else -> this.takeLast(32).toByteArray()
        }
}