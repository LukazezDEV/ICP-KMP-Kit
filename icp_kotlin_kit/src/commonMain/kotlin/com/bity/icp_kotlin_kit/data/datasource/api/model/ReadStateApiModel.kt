package com.bity.icp_kotlin_kit.data.datasource.api.model

import com.bity.icp_kotlin_kit.data.datasource.api.enum.ContentRequestType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class ReadStateApiModel(
    @SerialName("request_type") override val request_type: ContentRequestType,
    @SerialName("sender") override val sender: ByteArray,
    @SerialName("nonce") override val nonce: ByteArray,
    @SerialName("ingress_expiry") override val ingress_expiry: Long,
    @SerialName("paths") val paths: List<List<ByteArray>>
): ContentApiModel()