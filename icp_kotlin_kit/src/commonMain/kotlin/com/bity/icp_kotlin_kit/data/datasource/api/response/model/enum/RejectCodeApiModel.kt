package com.bity.icp_kotlin_kit.data.datasource.api.response.model.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO, remove PlaceHolder and fix mapping
@Serializable
enum class RejectCodeApiModel {
    PlaceHolder,
    @SerialName("1") SystemFatal,
    @SerialName("2") SystemTransient,
    @SerialName("3") DestinationInvalid,
    @SerialName("4") CanisterReject,
    @SerialName("5") CanisterError;

    companion object {
        fun valueFromErrorCode(value: Int): RejectCodeApiModel? =
            when(value) {
                1 -> SystemFatal
                2 -> SystemTransient
                3 -> DestinationInvalid
                4 -> CanisterReject
                5 -> CanisterError
                else -> null
            }
    }
}