package com.bity.icp_kotlin_kit.domain.model.nft

import com.bity.icp_kotlin_kit.domain.model.nft.metadata.ICPNFTMetadata
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

data class ICPNFTCollectionItem(
    val id: ICPBigInteger,
    val nftId: String,
    val metadata: ICPNFTMetadata?
)