package com.bity.icp_kotlin_kit.domain.use_case.nft

import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.repository.NFTCachedRepository
import com.bity.icp_kotlin_kit.bignum.ICPBigInteger

class FetchNFTCollectionTokenOwner internal constructor(
    private val nftCachedRepository: NFTCachedRepository
) {

    suspend operator fun invoke(
        collectionPrincipal: ICPPrincipal,
        nftId: ICPBigInteger
    ): ICPPrincipal? = nftCachedRepository.fetchNFTCollectionTokenOwner(
        collectionPrincipal = collectionPrincipal,
        nftId = nftId
    )

}