package com.bity.icp_kotlin_kit.bignum

import java.math.BigInteger as JBigInteger

actual class ICPBigInteger(private val delegate: JBigInteger) {
    actual fun toByteArray(): ByteArray = delegate.toByteArray()
    actual fun add(other: ICPBigInteger): ICPBigInteger = ICPBigInteger(delegate.add(other.delegate))
    actual fun subtract(other: ICPBigInteger): ICPBigInteger = ICPBigInteger(delegate.subtract(other.delegate))
    actual fun multiply(other: ICPBigInteger): ICPBigInteger = ICPBigInteger(delegate.multiply(other.delegate))
    actual fun divide(other: ICPBigInteger): ICPBigInteger = ICPBigInteger(delegate.divide(other.delegate))
    actual fun compareTo(other: ICPBigInteger): Int = delegate.compareTo(other.delegate)
    actual override fun toString(): String = delegate.toString()

    actual companion object {
        actual fun fromByteArray(bytes: ByteArray): ICPBigInteger = ICPBigInteger(JBigInteger(bytes))
        actual fun valueOf(long: Long): ICPBigInteger = ICPBigInteger(JBigInteger.valueOf(long))
        actual fun parseDecimal(s: String): ICPBigInteger = ICPBigInteger(JBigInteger(s))

        // JVM-only helper for existing code
        fun fromSignMagnitude(sign: Int, magnitude: ByteArray): ICPBigInteger {
            val bi = JBigInteger(magnitude)
            return if (sign >= 0) ICPBigInteger(bi) else ICPBigInteger(bi.negate())
        }
    }
}