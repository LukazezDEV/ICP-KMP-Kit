package com.bity.icp_kotlin_kit.bignum

expect class ICPBigInteger {
    fun toByteArray(): ByteArray
    fun add(other: ICPBigInteger): ICPBigInteger
    fun subtract(other: ICPBigInteger): ICPBigInteger
    fun multiply(other: ICPBigInteger): ICPBigInteger
    fun divide(other: ICPBigInteger): ICPBigInteger
    fun compareTo(other: ICPBigInteger): Int
    override fun toString(): String

    companion object {
        fun fromByteArray(bytes: ByteArray): ICPBigInteger
        fun valueOf(long: Long): ICPBigInteger
        fun parseDecimal(s: String): ICPBigInteger
    }
}