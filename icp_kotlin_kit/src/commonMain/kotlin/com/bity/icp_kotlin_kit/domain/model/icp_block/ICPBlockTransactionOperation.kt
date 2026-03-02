package com.bity.icp_kotlin_kit.domain.model.icp_block

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

sealed class ICPBlockTransactionOperation(
    val amount: ICPBigInteger,
    val fee: ICPBigInteger? = null
) {

    class Approve(
        val from: ByteArray,
        val allowance: ICPBigInteger,
        val expectedAllowance: ICPBigInteger?,
        fee: ICPBigInteger,
        val expiresAtNanos: ULong?,
        val spender: ByteArray
    ): ICPBlockTransactionOperation(
        amount = allowance,
        fee = fee
    )

    class Burn(
        val from: ByteArray,
        amount: ICPBigInteger,
        val spender: ByteArray?
    ): ICPBlockTransactionOperation(
        amount = amount
    )

    class Mint(
        val to: ByteArray,
        amount: ICPBigInteger
    ): ICPBlockTransactionOperation(
        amount = amount
    )

    class Transfer(
        val from: ByteArray,
        val to: ByteArray,
        amount: ICPBigInteger,
        fee: ICPBigInteger,
        val spender: ByteArray?
    ): ICPBlockTransactionOperation(
        amount = amount,
        fee = fee
    )
}