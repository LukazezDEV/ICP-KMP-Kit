package com.bity.icp_kotlin_kit.data.model.candid.deserializer

import com.bity.icp_kotlin_kit.data.model.candid.model.CandidPrimitiveType
import com.bity.icp_kotlin_kit.data.model.candid.model.KeyedContainerRowData
import com.bity.icp_kotlin_kit.cryptography.LEB128
import com.bity.icp_kotlin_kit.data.model.error.CandidDeserializationError
import okio.BufferedSource

internal sealed class CandidTypeTableData {

    data class Vector(val containedType: Int) : CandidTypeTableData()

    data class Option(val containedType: Int) : CandidTypeTableData()

    data class Record(val rows: List<KeyedContainerRowData>) : CandidTypeTableData()

    data class Variant(val rows: List<KeyedContainerRowData>) : CandidTypeTableData()

    data class Function(
        val inputTypes: List<Int>,
        val outputTypes: List<Int>,
        val annotations: List<ULong>
    ) : CandidTypeTableData()

    data class Service(
        val methods: List<ServiceMethod>
    ) : CandidTypeTableData() {

        class ServiceMethod(
            val name: String,
            val functionType: Int
        )
    }

    companion object {

        @Throws(CandidDeserializationError.InvalidPrimitive::class)
        fun decode(source: BufferedSource): CandidTypeTableData {
            val candidType: Int = LEB128.decodeSigned(source)
            val primitive = CandidPrimitiveType.candidPrimitiveTypeByValue(candidType)
                ?: throw CandidDeserializationError.InvalidPrimitive()

            return when (primitive) {

                CandidPrimitiveType.VECTOR -> {
                    val containedType: Int = LEB128.decodeSigned(source)
                    Vector(containedType)
                }

                CandidPrimitiveType.OPTION -> {
                    val containedType: Int = LEB128.decodeSigned(source)
                    Option(containedType)
                }

                CandidPrimitiveType.VARIANT -> {
                    val rows = decodeRows(source)
                    Variant(rows)
                }

                CandidPrimitiveType.RECORD -> {
                    val rows = decodeRows(source)
                    Record(rows)
                }

                CandidPrimitiveType.FUNCTION -> {
                    val nInputs: Int = LEB128.decodeUnsigned<Int>(source)
                    val inputTypes = (0 until nInputs).map {
                        LEB128.decodeSigned<Int>(source)
                    }

                    val nOutputs: Int = LEB128.decodeUnsigned<Int>(source)
                    val outputTypes = (0 until nOutputs).map {
                        LEB128.decodeSigned<Int>(source)
                    }

                    val nAnnotations: Int = LEB128.decodeUnsigned<Int>(source)
                    val annotations = (0 until nAnnotations).map {
                        LEB128.decodeUnsigned<ULong>(source)
                    }

                    Function(inputTypes, outputTypes, annotations)
                }

                CandidPrimitiveType.SERVICE -> {
                    val nMethods: Int = LEB128.decodeUnsigned(source)
                    val methods = (0 until nMethods).map {
                        val name = CandidDeserializer.readString(source)
                        val functionRef: Int = LEB128.decodeSigned(source)
                        Service.ServiceMethod(name, functionRef)
                    }
                    Service(methods)
                }

                else -> throw CandidDeserializationError.InvalidPrimitive()
            }
        }

        private fun decodeRows(source: BufferedSource): List<KeyedContainerRowData> {
            val nRows: Int = LEB128.decodeUnsigned(source)
            return (0 until nRows).map {
                KeyedContainerRowData(
                    hashedKey = LEB128.decodeUnsigned(source),
                    type = LEB128.decodeSigned(source)
                )
            }
        }
    }
}