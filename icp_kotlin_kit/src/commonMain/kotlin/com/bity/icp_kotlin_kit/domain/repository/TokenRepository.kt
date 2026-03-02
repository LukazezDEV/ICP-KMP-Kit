package com.bity.icp_kotlin_kit.domain.repository

import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.model.ICPTokenTransfer
import com.bity.icp_kotlin_kit.domain.model.arg.ICPTokenTransferArgs
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

internal interface TokenRepository {
    suspend fun getTokenCount(): ULong
    suspend fun fetchTokensPage(startAt: ULong, pageSize: ULong): List<ICPToken>
}