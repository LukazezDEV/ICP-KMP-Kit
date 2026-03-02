package com.bity.icp_kotlin_kit.domain.repository

import com.bity.icp_kotlin_kit.domain.model.icp_block.ICPBlock
import com.bity.icp_kotlin_kit.domain.model.request.TransferICPRequest
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

internal interface LedgerCanisterRepository {
    suspend fun transferICP(request: TransferICPRequest): ICPBigInteger
    suspend fun queryBlocks(start: ULong, length: ULong): List<ICPBlock>
}