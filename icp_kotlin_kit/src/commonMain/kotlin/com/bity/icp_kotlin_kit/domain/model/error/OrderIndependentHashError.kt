package com.bity.icp_kotlin_kit.domain.model.error

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

sealed class OrderIndependentHashError(
    errorMessage: String? = null,
    throwable: Throwable? = null
): Error(errorMessage, throwable) {
    class UnsupportedDataType(val value: Any): OrderIndependentHashError()
    class NonUtf8String(val string: String): OrderIndependentHashError()
    class NonPositiveNumber(val number: ICPBigInteger): OrderIndependentHashError()
    class NonASCIIString(val value: ByteArray): OrderIndependentHashError()
}