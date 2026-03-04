package com.bity.icp_kotlin_kit.data.datasource.api.response.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ReplyApiModel(
    @SerialName("arg") val arg: ByteArray
)