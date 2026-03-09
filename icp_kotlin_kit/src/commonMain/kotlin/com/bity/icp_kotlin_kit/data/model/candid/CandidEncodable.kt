package com.bity.icp_kotlin_kit.data.model.candid

import com.bity.icp_kotlin_kit.data.model.ValueToEncode

/**
 * Marks a type as convertible into a Candid record for encoding.
 *
 * Implementations return a map of field names to [ValueToEncode] entries.
 */
interface CandidEncodable {
    fun toCandidRecord(): Map<String, ValueToEncode>
}