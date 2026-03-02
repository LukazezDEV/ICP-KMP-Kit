package com.bity.icp_kotlin_kit.domain.model

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

sealed class ICPTokenTransfer {
    data class Height(
        val height: ICPBigInteger
    ): ICPTokenTransfer()
    data class Amount(
        val amount: String
    ): ICPTokenTransfer()
    data class TransactionId(
        val transactionId: String
    ): ICPTokenTransfer()
}