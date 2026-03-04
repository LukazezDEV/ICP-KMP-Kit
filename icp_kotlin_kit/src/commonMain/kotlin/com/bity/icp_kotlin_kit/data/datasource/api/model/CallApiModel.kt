package com.bity.icp_kotlin_kit.data.datasource.api.model

import com.bity.icp_kotlin_kit.data.datasource.api.enum.ContentRequestType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Need to use sneak case because of order independent hash
@Serializable
internal class CallApiModel(
    @SerialName("request_type") override val request_type: ContentRequestType,
    @SerialName("sender") override val sender: ByteArray,
    @SerialName("nonce") override val nonce: ByteArray,
    @SerialName("ingress_expiry") override val ingress_expiry: Long,
    @SerialName("method_name") val method_name: String,
    @SerialName("canister_id") val canister_id: ByteArray,
    @SerialName("arg") val arg: ByteArray
): ContentApiModel()