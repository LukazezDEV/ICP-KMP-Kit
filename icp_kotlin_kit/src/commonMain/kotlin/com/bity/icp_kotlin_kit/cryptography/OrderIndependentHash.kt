package com.bity.icp_kotlin_kit.cryptography

import com.bity.icp_kotlin_kit.data.datasource.api.model.ContentApiModel

/**
 * Order-independent hashing for request IDs.
 *
 * Implements the IC spec:
 * https://internetcomputer.org/docs/current/references/ic-interface-spec/#hash-of-map
 */
object OrderIndependentHash {

    operator fun invoke(
        value: Any,
        hashFunction: (ByteArray) -> ByteArray = SHA256::sha256
    ): ByteArray {
        return when (value) {
            is Map<*, *> -> hashMap(value as Map<String, Any>, hashFunction)
            is List<*> -> hashList(value as List<Any>, hashFunction)
            is ByteArray -> hashFunction(value)
            is String -> hashFunction(value.encodeToByteArray())

            // numeric types (explicit, no Number)
            is Byte, is Short, is Int, is Long,
            is UByte, is UShort, is UInt, is ULong,
            is Float, is Double ->
                hashFunction(encodeNumber(value))

            is Boolean -> hashFunction(value.toString().encodeToByteArray())
            is ContentApiModel -> hashMap(value.toMap(), hashFunction)
            else -> error("Unsupported type for hashing: ${value::class}")
        }
    }

    private fun encodeNumber(n: Any): ByteArray =
        when (n) {
            is Byte -> LEB128.encodeSigned(n)
            is Short -> LEB128.encodeSigned(n)
            is Int -> LEB128.encodeSigned(n)
            is Long -> LEB128.encodeSigned(n)

            is UByte -> LEB128.encodeUnsigned(n)
            is UShort -> LEB128.encodeUnsigned(n)
            is UInt -> LEB128.encodeUnsigned(n)
            is ULong -> LEB128.encodeUnsigned(n)

            is Float -> n.toBits().toByteArray()
            is Double -> n.toBits().toByteArray()

            else -> error("Unsupported number type: $n")
        }

    private fun Int.toByteArray(): ByteArray =
        byteArrayOf(
            (this and 0xFF).toByte(),
            ((this shr 8) and 0xFF).toByte(),
            ((this shr 16) and 0xFF).toByte(),
            ((this shr 24) and 0xFF).toByte()
        )

    private fun Long.toByteArray(): ByteArray =
        ByteArray(8) { i -> ((this shr (8 * i)) and 0xFF).toByte() }

    private fun hashMap(
        map: Map<String, Any>,
        hashFunction: (ByteArray) -> ByteArray
    ): ByteArray {
        // Sort keys lexicographically as required by the IC spec
        val sorted = map.toSortedMap()

        val hashedEntries = sorted.map { (key, value) ->
            val hashedKey = hashFunction(key.encodeToByteArray())
            val hashedValue = invoke(value, hashFunction)
            hashFunction(hashedKey + hashedValue)
        }

        // Hash concatenation of hashed entries
        return hashFunction(hashedEntries.fold(ByteArray(0)) { acc, h -> acc + h })
    }

    private fun hashList(
        list: List<Any>,
        hashFunction: (ByteArray) -> ByteArray
    ): ByteArray {
        val hashedItems = list.map { invoke(it, hashFunction) }
        return hashFunction(hashedItems.fold(ByteArray(0)) { acc, h -> acc + h })
    }
}