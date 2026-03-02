package com.bity.icp_kotlin_kit.data.model.candid.deserializer

import com.bity.icp_kotlin_kit.data.model.candid.model.*
import com.bity.icp_kotlin_kit.cryptography.LEB128
import com.bity.icp_kotlin_kit.data.model.error.CandidDeserializationError
import okio.BufferedSource

internal class CandidDecodableTypeTable(source: BufferedSource) {

    private val types: List<CandidType>
    val tableData: List<CandidTypeTableData>

    init {
        val typeCount: Int = LEB128.decodeUnsigned(source)
        val typeRange = 0 until typeCount

        tableData = typeRange.map { CandidTypeTableData.decode(source) }
        types = typeRange.map { buildType(it) }
    }

    private fun buildType(typeRef: Int): CandidType {
        return when (val type = tableData[typeRef]) {

            is CandidTypeTableData.Vector -> {
                val referenced = candidType(type.containedType)
                CandidType.Vector(referenced)
            }

            is CandidTypeTableData.Option -> {
                val referenced = candidType(type.containedType)
                CandidType.Option(referenced)
            }

            is CandidTypeTableData.Record -> {
                val rows = type.rows.map {
                    CandidKeyedType(
                        key = it.hashedKey,
                        type = candidType(it.type)
                    )
                }
                CandidType.Record(rows)
            }

            is CandidTypeTableData.Variant -> {
                val rows = type.rows.map {
                    CandidKeyedType(
                        key = it.hashedKey,
                        type = candidType(it.type)
                    )
                }
                CandidType.Variant(rows)
            }

            is CandidTypeTableData.Function -> {
                CandidType.Function(
                    signature = CandidFunctionSignature(
                        inputs = type.inputTypes.map { candidType(it) },
                        outputs = type.outputTypes.map { candidType(it) },
                        query = type.annotations.contains(0x01UL),
                        oneWay = type.annotations.contains(0x02UL),
                        compositeQuery = type.annotations.contains(0x03UL)
                    )
                )
            }

            is CandidTypeTableData.Service -> {
                val methods = type.methods.map {
                    val signature = candidType(it.functionType).functionSignature
                        ?: throw CandidDeserializationError.InvalidTypeReference()

                    CandidServiceSignatureMethod(
                        name = it.name,
                        functionSignature = signature
                    )
                }
                CandidType.Service(
                    CandidServiceSignature(methods)
                )
            }
        }
    }

    @Throws(
        CandidDeserializationError.InvalidTypeReference::class,
        CandidDeserializationError.InvalidPrimitive::class
    )
    private fun candidType(type: Int): CandidType {
        return if (type >= 0) {
            require(tableData.size > type) {
                throw CandidDeserializationError.InvalidTypeReference()
            }

            if (isTypeRecursive(type)) {
                CandidType.Named("$type")
            } else {
                buildType(type)
            }

        } else {
            val primitive = CandidPrimitiveType.candidPrimitiveTypeByValue(type)
                ?: throw CandidDeserializationError.InvalidPrimitive()

            CandidType.init(primitive)
                ?: throw CandidDeserializationError.InvalidPrimitive()
        }
    }

    @Throws(
        CandidDeserializationError.InvalidPrimitive::class,
        CandidDeserializationError.InvalidTypeReference::class
    )
    fun getTypeForReference(reference: Int): CandidType {
        if (reference < 0) {
            val primitive = CandidPrimitiveType.candidPrimitiveTypeByValue(reference)
                ?: throw CandidDeserializationError.InvalidPrimitive()

            return CandidType.init(primitive)
                ?: throw CandidDeserializationError.InvalidPrimitive()
        }

        require(types.size > reference) {
            throw CandidDeserializationError.InvalidTypeReference()
        }

        return if (isTypeRecursive(reference)) {
            CandidType.Named("$reference")
        } else {
            types[reference]
        }
    }

    private fun isTypeRecursive(typeRef: Int, visited: List<Int> = emptyList()): Boolean {
        if (typeRef < 0) return false
        if (visited.contains(typeRef)) return true

        val nextVisited = visited + typeRef

        return when (val type = tableData[typeRef]) {

            is CandidTypeTableData.Vector ->
                isTypeRecursive(type.containedType, nextVisited)

            is CandidTypeTableData.Option ->
                isTypeRecursive(type.containedType, nextVisited)

            is CandidTypeTableData.Record ->
                type.rows.any { isTypeRecursive(it.type, nextVisited) }

            is CandidTypeTableData.Variant ->
                type.rows.any { isTypeRecursive(it.type, nextVisited) }

            is CandidTypeTableData.Function -> {
                val all = type.inputTypes + type.outputTypes
                all.any { isTypeRecursive(it, nextVisited) }
            }

            is CandidTypeTableData.Service ->
                type.methods.any { isTypeRecursive(it.functionType, nextVisited) }
        }
    }
}