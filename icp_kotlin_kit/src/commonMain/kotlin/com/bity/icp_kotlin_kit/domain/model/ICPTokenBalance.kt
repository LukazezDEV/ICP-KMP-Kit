package com.bity.icp_kotlin_kit.domain.model

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger
import com.bity.icp_kotlin_kit.domain.model.enum.ICPTokenStandard

data class ICPTokenBalance(
    val token: ICPToken,
    val balance: ICPBigInteger
) {
    val decimalBalance: ICPDecimal = token.decimal(balance)
}