package com.bity.icp_kotlin_kit.data.repository.transaction

import com.bity.icp_kotlin_kit.data.generated_file.NNSICPIndexCanister
import com.bity.icp_kotlin_kit.domain.model.ICPAccount
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.model.error.GetAllTransactionsException
import com.bity.icp_kotlin_kit.domain.model.toDataModel
import com.bity.icp_kotlin_kit.domain.model.token_transaction.ICPTokenTransaction
import com.bity.icp_kotlin_kit.domain.model.token_transaction.ICPTokenTransactionDestination
import com.bity.icp_kotlin_kit.domain.model.token_transaction.ICPTokenTransactionOperation
import com.bity.icp_kotlin_kit.domain.repository.TransactionRepository
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

internal class IndexTransactionRepository(
    private val icpToken: ICPToken,
    private val indexCanister: NNSICPIndexCanister.NNSICPIndexCanisterService
): TransactionRepository {

    override suspend fun fetchAllTransactions(account: ICPAccount): List<ICPTokenTransaction> {
        val getAccountTransactionsArgs = NNSICPIndexCanister.GetAccountTransactionsArgs(
            account = NNSICPIndexCanister.Account(
                owner = account.principal.toDataModel(),
                subaccount = account.subAccountId.map { it.toUByte() }.toTypedArray()
            ),
            start = null,
            max_results = ICPBigInteger.parseDecimal("1000000")
        )
        val transactions = indexCanister.get_account_transactions(getAccountTransactionsArgs)
        return when(transactions) {
            is NNSICPIndexCanister.GetAccountIdentifierTransactionsResult.Err ->
                throw transactions.getAccountIdentifierTransactionsError.toDataModel()
            is NNSICPIndexCanister.GetAccountIdentifierTransactionsResult.Ok ->
                transactions.getAccountIdentifierTransactionsResponse
                    .transactions
                    .map { it.toDomainModel() }
        }
    }

    private fun NNSICPIndexCanister.TransactionWithId.toDomainModel(): ICPTokenTransaction {
        val icrc1Memo = transaction.icrc1_memo
            ?.map { it.toByte() }
            ?.toByteArray()

        val operation: ICPTokenTransactionOperation
        val spender: ICPTokenTransactionDestination?
        val amount: ICPBigInteger
        val fee: ICPBigInteger

        when(val op = this.transaction.operation) {

            is NNSICPIndexCanister.Operation.Approve -> {
                operation = ICPTokenTransactionOperation.Approve(
                    from = ICPTokenTransactionDestination.AccountId(op.from),
                    expectedAllowance = op.expected_allowance?.let { ICPBigInteger.parseDecimal(it.e8s.toString()) },
                    expires = op.expires_at?.timestamp_nanos?.toLong()
                )
                amount = ICPBigInteger.parseDecimal(op.allowance.e8s.toString())
                fee = ICPBigInteger.parseDecimal(op.fee.e8s.toString())
                spender = ICPTokenTransactionDestination.AccountId(op.spender)
            }

            is NNSICPIndexCanister.Operation.Burn -> {
                operation = ICPTokenTransactionOperation.Burn(
                    from = ICPTokenTransactionDestination.AccountId(op.from)
                )
                amount = ICPBigInteger.parseDecimal(op.amount.e8s.toString())
                fee = ICPBigInteger.ZERO
                spender = op.spender?.let {
                    ICPTokenTransactionDestination.AccountId(it)
                }
            }

            is NNSICPIndexCanister.Operation.Mint -> {
                operation = ICPTokenTransactionOperation.Mint(
                    to = ICPTokenTransactionDestination.AccountId(op.to)
                )
                amount = ICPBigInteger.parseDecimal(op.amount.e8s.toString())
                fee = ICPBigInteger.ZERO
                spender = null
            }

            is NNSICPIndexCanister.Operation.Transfer -> {
                operation = ICPTokenTransactionOperation.Transfer(
                    from = ICPTokenTransactionDestination.AccountId(op.from),
                    to = ICPTokenTransactionDestination.AccountId(op.to)
                )
                amount = ICPBigInteger.parseDecimal(op.amount.e8s.toString())
                fee = ICPBigInteger.parseDecimal(op.fee.e8s.toString())
                spender = op.spender?.let { ICPTokenTransactionDestination.AccountId(it) }
            }
        }

        return ICPTokenTransaction(
            blockIndex = ICPBigInteger.parseDecimal(id.toString()),
            operation = operation,
            icrc1Memo = icrc1Memo,
            memo = ICPBigInteger.parseDecimal(transaction.memo.toString()),
            amount = amount,
            fee = fee,
            createdNanos = transaction.created_at_time?.timestamp_nanos,
            timeStampNanos = transaction.timestamp?.timestamp_nanos,
            spender = spender,
            token = icpToken
        )
    }

    private fun NNSICPIndexCanister.GetAccountIdentifierTransactionsError.toDataModel(): Exception =
        GetAllTransactionsException(
            token = icpToken,
            errorMessage = message
        )
}
