package com.bity.icp_kotlin_kit.domain.model.token_transaction

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

sealed class ICPTokenTransactionOperation {
    data class Mint(val to: ICPTokenTransactionDestination): ICPTokenTransactionOperation()
    data class Burn(val from: ICPTokenTransactionDestination): ICPTokenTransactionOperation()
    data class Approve(
        val from: ICPTokenTransactionDestination,
        val expectedAllowance: ICPBigInteger?,
        val expires: Long?
    ): ICPTokenTransactionOperation()
    data class Transfer(
        val from: ICPTokenTransactionDestination,
        val to: ICPTokenTransactionDestination
    ): ICPTokenTransactionOperation()
}