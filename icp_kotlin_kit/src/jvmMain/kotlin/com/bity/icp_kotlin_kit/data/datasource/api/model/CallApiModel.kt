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
): ContentApiModel() {
    override fun toMap(): Map<String, Any> = mapOf(
        "request_type" to request_type.type,
        "sender" to sender,
        "nonce" to nonce,
        "ingress_expiry" to ingress_expiry,
        "method_name" to method_name,
        "canister_id" to canister_id,
        "arg" to arg
    )
}