package com.bity.icp_kotlin_kit.data.datasource.api.model

import com.bity.icp_kotlin_kit.cryptography.OrderIndependentHash
import com.bity.icp_kotlin_kit.data.datasource.api.enum.ContentRequestType

// Need to use sneak case because of order independent hash
internal abstract class ContentApiModel {

    abstract val request_type: ContentRequestType
    abstract val sender: ByteArray
    abstract val nonce: ByteArray
    abstract val ingress_expiry: Long

    abstract fun toMap(): Map<String, Any>

    fun calculateRequestId(): ByteArray = OrderIndependentHash(this)
}