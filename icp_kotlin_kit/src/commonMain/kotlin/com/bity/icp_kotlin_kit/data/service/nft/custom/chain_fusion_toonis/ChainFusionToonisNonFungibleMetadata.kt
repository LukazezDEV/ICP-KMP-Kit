package com.bity.icp_kotlin_kit.data.service.nft.custom.chain_fusion_toonis

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChainFusionToonisNonFungibleMetadata(
    @SerialName("url") val url: String,
    @SerialName("thumb") val thumb: String,
    @SerialName("mimeType") val mimeType: String
)