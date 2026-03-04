package com.bity.icp_kotlin_kit.bignum

expect class ICPBigInteger {

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

    fun toByteArray(): ByteArray
    fun add(other: ICPBigInteger): ICPBigInteger
    fun subtract(other: ICPBigInteger): ICPBigInteger
    fun multiply(other: ICPBigInteger): ICPBigInteger
    fun divide(other: ICPBigInteger): ICPBigInteger
    fun compareTo(other: ICPBigInteger): Int

    fun shiftLeft(n: Int): ICPBigInteger
    fun shiftRight(n: Int): ICPBigInteger
    fun and(other: ICPBigInteger): ICPBigInteger
    fun or(other: ICPBigInteger): ICPBigInteger

    fun toByte(): Byte
    fun toShort(): Short
    fun toInt(): Int
    fun toLong(): Long
    fun toFloat(): Float
    fun signum(): Int

    override fun toString(): String

    companion object {
        val ZERO: ICPBigInteger
        val ONE: ICPBigInteger

        fun fromByteArray(bytes: ByteArray): ICPBigInteger
        fun fromSignMagnitude(sign: Int, magnitude: ByteArray): ICPBigInteger
        fun valueOf(long: Long): ICPBigInteger
        fun parseDecimal(s: String): ICPBigInteger
    }
}
