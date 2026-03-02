package com.bity.icp_kotlin_kit.domain.model.error

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

sealed class OIHashError(errorMessage: String? = null): Error(errorMessage)

class UnsupportedDataType(val value: Any): OIHashError()
class NonUTF8String(val string: String): OIHashError()
class NonPositiveNumber(val number: ICPBigInteger): OIHashError()
class NonASCIIString(val value: ByteArray): OIHashError()