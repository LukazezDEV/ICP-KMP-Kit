package com.bity.icp_kotlin_kit.domain.model.token_transaction

import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

class ICPTokenTransaction(
    val blockIndex: ICPBigInteger
    val operation: ICPTokenTransactionOperation,
    val memo: ICPBigInteger?,
    val icrc1Memo: ByteArray?,
    val amount: ICPBigInteger,
    val fee: ICPBigInteger,
    val createdNanos: ULong?,
    val timeStampNanos: ULong?,
    val spender: ICPTokenTransactionDestination?,
    val token: ICPToken
) {
    val from = when(operation) {
        is ICPTokenTransactionOperation.Approve -> operation.from
        is ICPTokenTransactionOperation.Burn -> operation.from
        is ICPTokenTransactionOperation.Mint -> null
        is ICPTokenTransactionOperation.Transfer -> operation.from
    }
    val to = when(operation) {
        is ICPTokenTransactionOperation.Approve,
        is ICPTokenTransactionOperation.Burn -> null
        is ICPTokenTransactionOperation.Mint -> operation.to
        is ICPTokenTransactionOperation.Transfer -> operation.to
    }
}
