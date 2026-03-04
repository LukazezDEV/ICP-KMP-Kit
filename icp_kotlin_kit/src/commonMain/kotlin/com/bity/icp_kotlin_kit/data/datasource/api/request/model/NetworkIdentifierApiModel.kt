package com.bity.icp_kotlin_kit.data.datasource.api.request.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class NetworkIdentifierApiModel(
    @SerialName("blockchain") val blockchain: String,
    @SerialName("network") val network: String
)