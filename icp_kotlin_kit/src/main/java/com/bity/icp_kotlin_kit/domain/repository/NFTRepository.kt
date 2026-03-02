package com.bity.icp_kotlin_kit.domain.repository

import com.bity.icp_kotlin_kit.domain.model.nft.ICPNFTDetails
import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.model.nft.ICPNFTCollectionItem
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

interface NFTRepository {
    suspend fun fetchIds(
        prev: ICPBigInteger? = null,
        take: ICPBigInteger? = null
    ): List<ICPBigInteger>
    suspend fun fetchNFTs(collectionPrincipal: ICPPrincipal): List<ICPNFTCollectionItem>
    suspend fun fetchNFT(
        collectionPrincipal: ICPPrincipal,
        nftId: ICPBigInteger
    ) : ICPNFTCollectionItem
    suspend fun fetchOwner(
        collectionPrincipal: ICPPrincipal,
        nftId: ICPBigInteger
    ) : ICPPrincipal?
    suspend fun fetchUserHoldings(principal: ICPPrincipal): List<ICPNFTDetails>
}