package com.bity.icp_kotlin_kit.domain.model

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

data class ICPDecimal(
    val unscaled: ICPBigInteger,
    val scale: Int
) {

    operator fun plus(other: ICPDecimal): ICPDecimal {
        require(scale == other.scale) { "Scales must match for addition" }
        return ICPDecimal(unscaled.add(other.unscaled), scale)
    }

    operator fun minus(other: ICPDecimal): ICPDecimal {
        require(scale == other.scale) { "Scales must match for subtraction" }
        return ICPDecimal(unscaled.subtract(other.unscaled), scale)
    }

    operator fun times(other: ICPDecimal): ICPDecimal {
        // Multiply unscaled values, sum scales
        return ICPDecimal(unscaled.multiply(other.unscaled), scale + other.scale)
    }

    operator fun div(other: ICPDecimal): ICPDecimal {
        // Simple division: scale the numerator to preserve precision
        val factor = ICPBigInteger.parseDecimal("1" + "0".repeat(scale))
        return ICPDecimal(unscaled.multiply(factor).divide(other.unscaled), scale)
    }

    override fun toString(): String {
        val s = unscaled.toString().padStart(scale + 1, '0')
        val integerPart = s.dropLast(scale)
        val fractionalPart = s.takeLast(scale)
        return "$integerPart.$fractionalPart"
    }

    companion object {
        fun fromLong(value: Long, scale: Int): ICPDecimal {
            val unscaled = ICPBigInteger.valueOf(value).multiply(ICPBigInteger.parseDecimal("1" + "0".repeat(scale)))
            return ICPDecimal(unscaled, scale)
        }
    }
}