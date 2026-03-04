package com.bity.icp_kotlin_kit.data.datasource.api.request.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AccountIdentifierApiModel(
    @SerialName("address") val address: String
)