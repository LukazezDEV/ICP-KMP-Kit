package com.bity.icp_kotlin_kit.data.datasource.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ReadStateResponse(
    @SerialName("certificate") val certificate: ByteArray
)