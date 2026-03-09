package com.bity.icp_kotlin_kit.data.datasource.api.request

import com.bity.icp_kotlin_kit.data.datasource.api.model.ContentApiModel

internal class ICPRequestEnvelope(
    val content: ContentApiModel,
    val senderPubKey: ByteArray? = null,
    val senderSig: ByteArray? = null
)