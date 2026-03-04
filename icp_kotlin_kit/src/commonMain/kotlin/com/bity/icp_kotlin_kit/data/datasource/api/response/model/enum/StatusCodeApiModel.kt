package com.bity.icp_kotlin_kit.data.datasource.api.response.model.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StatusCodeApiModel {
    @SerialName(value = "received") Received,
    @SerialName(value = "processing") Processing,
    @SerialName(value = "replied") Replied,
    @SerialName(value = "rejected") Rejected,
    @SerialName(value = "done") Done
}