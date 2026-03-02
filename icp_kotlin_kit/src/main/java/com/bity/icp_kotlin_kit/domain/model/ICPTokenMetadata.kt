package com.bity.icp_kotlin_kit.domain.model

import com.bity.icp_kotlin_kit.data.generated_file.DIP20
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

internal data class ICPTokenMetadata(
    val name: String,
    val symbol: String,
    val decimals: Int,
    val totalSupply: ICPBigInteger,
    val logoUrl: String?,
    val fee: ICPBigInteger
)

internal fun DIP20.Metadata.toDomainModel(): ICPTokenMetadata =
    ICPTokenMetadata(
        name = name,
        symbol = symbol,
        decimals = decimals.toByte().toInt(),
        totalSupply = totalSupply,
        logoUrl = logo,
        fee = fee
    )