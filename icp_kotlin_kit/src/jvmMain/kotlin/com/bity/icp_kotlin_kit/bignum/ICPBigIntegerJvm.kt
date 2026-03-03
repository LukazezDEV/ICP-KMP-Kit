package com.bity.icp_kotlin_kit.bignum

import java.math.BigInteger as JBigInteger

actual class ICPBigInteger(private val delegate: JBigInteger) {

    actual fun toByteArray(): ByteArray = delegate.toByteArray()

    actual fun add(other: ICPBigInteger) =
        ICPBigInteger(delegate.add(other.delegate))

    actual fun subtract(other: ICPBigInteger) =
        ICPBigInteger(delegate.subtract(other.delegate))

    actual fun multiply(other: ICPBigInteger) =
        ICPBigInteger(delegate.multiply(other.delegate))

    actual fun divide(other: ICPBigInteger) =
        ICPBigInteger(delegate.divide(other.delegate))

    actual fun compareTo(other: ICPBigInteger) =
        delegate.compareTo(other.delegate)

    actual fun shiftLeft(n: Int) =
        ICPBigInteger(delegate.shiftLeft(n))

    actual fun shiftRight(n: Int) =
        ICPBigInteger(delegate.shiftRight(n))

    actual fun and(other: ICPBigInteger) =
        ICPBigInteger(delegate.and(other.delegate))

    actual fun or(other: ICPBigInteger) =
        ICPBigInteger(delegate.or(other.delegate))

    actual fun toByte(): Byte = delegate.toByte()
    actual fun toShort(): Short = delegate.toShort()
    actual fun toInt(): Int = delegate.toInt()
    actual fun toLong(): Long = delegate.toLong()

    // ⭐ NEW
    actual fun toFloat(): Float = delegate.toFloat()
    actual fun signum(): Int = delegate.signum()

    actual override fun toString(): String = delegate.toString()

    actual companion object {
        actual val ZERO = ICPBigInteger(JBigInteger.ZERO)
        actual val ONE = ICPBigInteger(JBigInteger.ONE)

        actual fun fromByteArray(bytes: ByteArray) =
            ICPBigInteger(JBigInteger(bytes))

        actual fun fromSignMagnitude(sign: Int, magnitude: ByteArray): ICPBigInteger {
            val bigInt = JBigInteger(sign, magnitude)
            return ICPBigInteger(bigInt)
        }
        actual fun valueOf(long: Long) =
            ICPBigInteger(JBigInteger.valueOf(long))

        actual fun parseDecimal(s: String) =
            ICPBigInteger(JBigInteger(s))
    }
}
