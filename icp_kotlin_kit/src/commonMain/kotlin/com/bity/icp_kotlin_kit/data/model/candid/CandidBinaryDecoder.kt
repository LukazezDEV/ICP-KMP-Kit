package com.bity.icp_kotlin_kit.data.model.candid

import com.bity.icp_kotlin_kit.data.model.candid.model.CandidValue

/**
 * Multiplatform entry point for decoding raw Candid bytes.
 *
 * Returns a list of parsed [CandidValue] produced by the platform-specific
 * binary decoder.
 */
expect object CandidBinaryDecoder {
    fun decode(bytes: ByteArray): List<CandidValue>
}
