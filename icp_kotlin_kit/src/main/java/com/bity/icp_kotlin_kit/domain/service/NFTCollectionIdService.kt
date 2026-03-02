package com.bity.icp_kotlin_kit.domain.service

import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

interface NFTCollectionIdService {
    fun getNFTCollectionItemId(
        canisterBytes: ByteArray,
        tokenIndex: ICPBigInteger
    ): String
}