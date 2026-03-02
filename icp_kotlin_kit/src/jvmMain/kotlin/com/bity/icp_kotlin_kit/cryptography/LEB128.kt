package com.bity.icp_kotlin_kit.cryptography

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger
import okio.BufferedSource

internal object LEB128 {

    fun encodeUnsigned(value: Number): ByteArray =
        when (value::class) {
            Byte::class -> encodeUnsignedBigInt(ICPBigInteger.valueOf((value as Byte).toLong()))
            Short::class -> encodeUnsignedBigInt(ICPBigInteger.valueOf((value as Short).toLong()))
            Int::class -> encodeUnsignedBigInt(ICPBigInteger.valueOf((value as Int).toLong()))
            Long::class -> encodeUnsignedBigInt(ICPBigInteger.valueOf((value as Long)))
            ICPBigInteger::class -> encodeUnsignedBigInt(value as ICPBigInteger)
            else -> throw Exception("Class ${value::class} not supported")
        }

    fun encodeUnsigned(byte: UByte): ByteArray =
        encodeUnsigned(byte.toLong())

    fun encodeUnsigned(uShort: UShort): ByteArray =
        encodeUnsigned(uShort.toLong())

    fun encodeUnsigned(uInt: UInt): ByteArray =
        encodeUnsigned(uInt.toLong())

    fun encodeUnsigned(uLong: ULong): ByteArray =
        encodeUnsigned(uLong.toLong())

    private fun encodeUnsignedBigInt(bigInt: ICPBigInteger): ByteArray {
        var value = bigInt
        val bytes = mutableListOf<Byte>()
        val mask7F = ICPBigInteger.valueOf(0x7F)

        do {
            var byte = value.and(mask7F).toByte()
            value = value.shiftRight(7)

            if (value != ICPBigInteger.valueOf(0)) {
                byte = (byte.toInt() or 0x80).toByte()
            }

            bytes.add(byte)
        } while (value != ICPBigInteger.valueOf(0))

        return bytes.toByteArray()
    }

    fun encodeSigned(value: Number): ByteArray =
        when (value::class) {
            Byte::class -> encodeSignedBigInt(ICPBigInteger.valueOf((value as Byte).toLong()))
            Short::class -> encodeSignedBigInt(ICPBigInteger.valueOf((value as Short).toLong()))
            Int::class -> encodeSignedBigInt(ICPBigInteger.valueOf((value as Int).toLong()))
            Long::class -> encodeSignedBigInt(ICPBigInteger.valueOf((value as Long)))
            ICPBigInteger::class -> encodeSignedBigInt(value as ICPBigInteger)
            else -> throw Exception("Class ${value::class} not supported")
        }

    private fun encodeSignedBigInt(bigInt: ICPBigInteger): ByteArray {
        var value = bigInt
        val bytes = mutableListOf<Byte>()
        val mask7F = ICPBigInteger.valueOf(0x7F)
        val minusOne = ICPBigInteger.valueOf(-1)

        var more = true
        while (more) {
            var byte = value.and(mask7F).toByte()
            value = value.shiftRight(7)

            val signBit = (byte.toInt() shr 6) and 1

            if ((value == ICPBigInteger.valueOf(0) && signBit == 0) ||
                (value == minusOne && signBit == 1)
            ) {
                more = false
            } else {
                byte = (byte.toInt() or 0x80).toByte()
            }

            bytes.add(byte)
        }

        return bytes.toByteArray()
    }

    inline fun <reified T> decodeUnsigned(source: BufferedSource): T {
        var result = ICPBigInteger.valueOf(0)
        var shift = 0

        var byte: Int
        do {
            byte = source.readByte().toInt() and 0xFF

            result = result.or(
                ICPBigInteger.valueOf((byte and 0x7F).toLong()).shiftLeft(shift)
            )

            shift += 7
        } while ((byte and 0x80) != 0)

        return when (T::class) {
            UByte::class -> result.toByte().toUByte()
            UShort::class -> result.toShort().toUShort()
            UInt::class -> result.toInt().toUInt()
            ULong::class -> result.toLong().toULong()
            Int::class -> result.toInt()
            Long::class -> result.toLong()
            ICPBigInteger::class -> result
            else -> throw IllegalArgumentException("Unsupported type: ${T::class.simpleName}")
        } as T
    }

    inline fun <reified T : Number> decodeSigned(source: BufferedSource): T {
        var result = ICPBigInteger.valueOf(0)
        var shift = 0

        var byte: Int
        do {
            byte = source.readByte().toInt() and 0xFF

            result = result.or(
                ICPBigInteger.valueOf((byte and 0x7F).toLong()).shiftLeft(shift)
            )

            shift += 7
        } while ((byte and 0x80) != 0)

        if ((byte and 0x40) != 0) {
            result = result.or(ICPBigInteger.valueOf(-1).shiftLeft(shift))
        }

        return when (T::class) {
            Byte::class -> result.toByte()
            Short::class -> result.toShort()
            Int::class -> result.toInt()
            Long::class -> result.toLong()
            Float::class -> result.toFloat()
            ICPBigInteger::class -> result
            else -> throw IllegalArgumentException("Unsupported type: ${T::class.simpleName}")
        } as T
    }
}
