package com.bity.icp_kotlin_kit.data.model.candid.model

sealed class CandidOption(
    val value: CandidValue?,
    val containedType: CandidType
) {
    class None(type: CandidType): CandidOption(
        value = null,
        containedType = type
    )
    class Some(wrapped: CandidValue): CandidOption(
        value = wrapped,
        containedType = wrapped.candidType
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CandidOption) return false

        // None vs Some mismatch
        if (this::class != other::class) return false

        // Compare contained type
        if (containedType != other.containedType) return false

        // Compare wrapped value (null-safe)
        return value == other.value
    }

    override fun hashCode(): Int {
        var result = containedType.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
}