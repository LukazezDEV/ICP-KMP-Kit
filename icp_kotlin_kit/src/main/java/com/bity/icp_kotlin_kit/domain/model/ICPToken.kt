package com.bity.icp_kotlin_kit.domain.model

import com.bity.icp_kotlin_kit.domain.model.enum.ICPTokenStandard

data class ICPToken(
    val standard: ICPTokenStandard,
    val canister: ICPPrincipal,
    val name: String,
    val decimals: Int,
    val symbol: String,
    val spam: Boolean,
    val logo: String?,
) {
    fun decimal(amount: ICPBigInteger): ICPDecimal =
        ICPDecimal(amount, decimals)
}