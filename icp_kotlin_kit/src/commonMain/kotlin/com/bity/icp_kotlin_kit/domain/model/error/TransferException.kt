package com.bity.icp_kotlin_kit.domain.model.error

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

sealed class TransferException(errorMessage: String? = null) : Exception(errorMessage) {
    data object InsufficientAllowance : TransferException()
    data object InsufficientBalance : TransferException()
    data object ErrorOperationStyle : TransferException()
    data object Unauthorized : TransferException()
    data object LedgerTrap : TransferException()
    data object ErrorTo : TransferException()
    class Other(
        val string: String
    ): TransferException()
    data object BlockUsed : TransferException()
    data object AmountTooSmall : TransferException()
    class BadFee(expectedFee: ICPBigInteger): TransferException("Expected fee: $expectedFee")
    class BadBurn(minBurnAmount: ICPBigInteger): TransferException("Min burn amount: $minBurnAmount")
    class InsufficientFunds(val balance: ICPBigInteger): TransferException("Insufficient funds: $balance")
    data object TooOld : TransferException()
    class CreatedInFuture(ledgerTime: ULong)
        : TransferException("Transaction created in future, ledger time: $ledgerTime")
    class Duplicate(duplicateOf: ICPBigInteger): TransferException("Transaction duplicate of $duplicateOf")
    data object TemporarilyUnavailable : TransferException()
    class GenericError(errorCode: ICPBigInteger, message: String):
        TransferException("Generic error: [$errorCode] - $message")
}