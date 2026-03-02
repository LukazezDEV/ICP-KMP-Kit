package com.bity.icp_kotlin_kit.domain.model.request

import com.bity.icp_kotlin_kit.domain.model.ICPAccount
import com.bity.icp_kotlin_kit.domain.model.ICPSigningPrincipal
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

data class TransferICPRequest(
    val sendingAccount: ICPAccount,
    val receivingAddress: String,
    val amount: ICPBigInteger,
    val signingPrincipal: ICPSigningPrincipal,
    val fee: ICPBigInteger,
    val memo: ULong = 0UL
)