package com.bity.icp_kotlin_kit.data.datasource.api.enum

import com.bity.icp_kotlin_kit.data.datasource.api.model.ICPRequestApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal enum class ContentRequestType(val type: String) {
    @SerialName("call") Call("call"),
    @SerialName("query") Query("query"),
    @SerialName("read_state") ReadState("read_state");

    companion object {
        internal fun fromICPRequestApiModel(request: ICPRequestApiModel): ContentRequestType =
            when(request) {
                is ICPRequestApiModel.Call -> Call
                is ICPRequestApiModel.Query -> Query
                is ICPRequestApiModel.ReadState -> ReadState
            }
    }
}