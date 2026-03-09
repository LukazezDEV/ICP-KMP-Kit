package com.bity.icp_kotlin_kit.data.model.candid

import com.bity.icp_kotlin_kit.data.model.candid.deserializer.CandidDeserializer
import com.bity.icp_kotlin_kit.data.model.candid.model.CandidValue

/**
 * JVM implementation of the Candid binary decoder.
 *
 * Converts raw Candid-encoded bytes into a list of [CandidValue] by delegating
 * to the JVM Candid deserializer.
 */
actual object CandidBinaryDecoder {
    actual fun decode(bytes: ByteArray): List<CandidValue> =
        CandidDeserializer.decode(bytes)
}