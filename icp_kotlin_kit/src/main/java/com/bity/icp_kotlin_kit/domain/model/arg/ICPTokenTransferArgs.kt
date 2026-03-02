package com.bity.icp_kotlin_kit.domain.model.arg

import com.bity.icp_kotlin_kit.domain.model.ICPAccount
import com.bity.icp_kotlin_kit.domain.model.ICPSigningPrincipal
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

class ICPTokenTransferArgs(
    val token: ICPToken,
    val sender: ICPSigningPrincipal,
    val from: ICPAccount,
    val to: ICPAccount,
    val amount: ICPBigInteger,
    val fee: ICPBigInteger?,
    val icrc1Memo: ByteArray?,
    val createdAtMillis: Long?
)