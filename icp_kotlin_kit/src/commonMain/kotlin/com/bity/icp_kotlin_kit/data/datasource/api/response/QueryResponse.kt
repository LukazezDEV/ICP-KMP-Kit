package com.bity.icp_kotlin_kit.data.datasource.api.response

import com.bity.icp_kotlin_kit.data.datasource.api.response.model.ReplyApiModel
import com.bity.icp_kotlin_kit.data.datasource.api.response.model.enum.RejectCodeApiModel
import com.bity.icp_kotlin_kit.data.datasource.api.response.model.enum.StatusCodeApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class QueryResponse(
    @SerialName("status") val status: StatusCodeApiModel,
    @SerialName("reply") val reply: ReplyApiModel?,
    @SerialName("reject_code") val rejectCode: RejectCodeApiModel?,
    @SerialName("reject_message") val rejectMessage: String?,
    @SerialName("error_code") val errorCode: String?
)