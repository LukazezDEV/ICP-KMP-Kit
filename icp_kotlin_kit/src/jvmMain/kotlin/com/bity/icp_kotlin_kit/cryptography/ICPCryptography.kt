package com.bity.icp_kotlin_kit.cryptography

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger
import com.bity.icp_kotlin_kit.domain.model.icp_block.ICPBlockTransaction
import com.bity.icp_kotlin_kit.domain.model.icp_block.ICPBlockTransactionOperation
import com.bity.icp_kotlin_kit.util.cbor.UnsignedNumberCBORSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory

private val objectMapper = ObjectMapper(CBORFactory())

@OptIn(ExperimentalStdlibApi::class)
internal fun ICPCryptography.transactionHash(icpBlockTransaction: ICPBlockTransaction): ByteArray {
    val serialized = icpBlockTransaction.cborHexString.hexToByteArray()
    return SHA256.sha256(serialized)
}

internal fun ICPCryptography.transactionHash(
    operation: ICPBlockTransactionOperation,
    memo: ICPBigInteger,
    createdNanos: ULong
): ByteArray = transactionHash(
    ICPBlockTransaction(
        memo = memo.toLong().toULong(),
        createdNanos = createdNanos,
        operation = operation
    )
)

private val ICPBlockTransaction.cborHexString: String
    get() = "a300a1${operation.cbor}" +
            "01${UnsignedNumberCBORSerializer.serialize(memo)}" +
            "02a100${UnsignedNumberCBORSerializer.serialize(createdNanos ?: 0UL)}"

@OptIn(ExperimentalStdlibApi::class)
private val ICPBlockTransactionOperation.cbor
    get() = when(this) {
        is ICPBlockTransactionOperation.Burn ->
            "00a200${objectMapper.writeValueAsBytes(this.from.toHexString()).toHexString()}" +
                    "01${UnsignedNumberCBORSerializer.serialize(this.amount.toLong().toULong())}"
        is ICPBlockTransactionOperation.Mint ->
            "01a200${objectMapper.writeValueAsBytes(this.to.toHexString()).toHexString()}" +
                    "01${UnsignedNumberCBORSerializer.serialize(this.amount.toLong().toULong())}"
        is ICPBlockTransactionOperation.Transfer ->
            "02a4" +
                    "00${objectMapper.writeValueAsBytes(this.from.toHexString()).toHexString()}" +
                    "01${objectMapper.writeValueAsBytes(this.to.toHexString()).toHexString()}" +
                    "02a100${UnsignedNumberCBORSerializer.serialize(this.amount.toLong().toULong())}" +
                    "03a100${UnsignedNumberCBORSerializer.serialize(this.fee!!.toLong().toULong())}"

        // TODO: Can not find any docs for this
        is ICPBlockTransactionOperation.Approve -> null
    }