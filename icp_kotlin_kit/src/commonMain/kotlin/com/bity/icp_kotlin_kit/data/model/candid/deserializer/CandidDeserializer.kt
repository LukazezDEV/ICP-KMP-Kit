package com.bity.icp_kotlin_kit.data.model.candid.deserializer

import com.bity.icp_kotlin_kit.data.model.candid.model.*
import com.bity.icp_kotlin_kit.data.model.candid.serializer.CandidSerializer
import com.bity.icp_kotlin_kit.cryptography.LEB128
import com.bity.icp_kotlin_kit.data.model.error.CandidDeserializationError
import okio.Buffer
import okio.BufferedSource

internal object CandidDeserializer {

    @Throws(
        CandidDeserializationError.InvalidPrefix::class,
        CandidDeserializationError.InvalidUTF8String::class,
        CandidDeserializationError.InvalidTypeReference::class,
        CandidDeserializationError.UnSerializedBytesLeft::class,
    )
    fun decode(data: ByteArray): List<CandidValue> {
        require(
            data.take(CandidSerializer.magicBytes.size).toByteArray()
                .contentEquals(CandidSerializer.magicBytes)
        ) { throw CandidDeserializationError.InvalidPrefix() }

        val unwrapped = data.drop(CandidSerializer.magicBytes.size).toByteArray()
        val source = Buffer().write(unwrapped)

        val typeTable = CandidDecodableTypeTable(source)

        val nValues: Int = LEB128.decodeUnsigned(source)
        val typeRefs = (0 until nValues).map {
            LEB128.decodeSigned<Int>(source)
        }

        val values = typeRefs.map { typeRef ->
            decodeValue(typeRef, source, typeTable)
        }

        require(source.exhausted()) {
            throw CandidDeserializationError.UnSerializedBytesLeft()
        }

        return values
    }

    private fun decodeValue(
        typeRef: Int,
        source: BufferedSource,
        table: CandidDecodableTypeTable
    ): CandidValue {
        val primitive = CandidPrimitiveType.candidPrimitiveTypeByValue(typeRef)
        return if (primitive != null) {
            decodePrimitiveValue(primitive, source)
        } else {
            decodeTypeTableValue(typeRef, source, table)
        }
    }

    private fun decodePrimitiveValue(
        primitive: CandidPrimitiveType,
        source: BufferedSource
    ): CandidValue {
        return when (primitive) {
            CandidPrimitiveType.NULL -> CandidValue.Null
            CandidPrimitiveType.BOOL -> CandidValue.Bool(source.readByte().toInt() != 0)

            CandidPrimitiveType.NATURAL ->
                CandidValue.Natural(LEB128.decodeUnsigned(source))

            CandidPrimitiveType.INTEGER ->
                CandidValue.Integer(LEB128.decodeSigned(source))

            CandidPrimitiveType.NATURAL8 ->
                CandidValue.Natural8(source.readByte().toUByte())

            CandidPrimitiveType.NATURAL16 ->
                CandidValue.Natural16(source.readShortLe().toUShort())

            CandidPrimitiveType.NATURAL32 ->
                CandidValue.Natural32(source.readIntLe().toUInt())

            CandidPrimitiveType.NATURAL64 ->
                CandidValue.Natural64(source.readLongLe().toULong())

            CandidPrimitiveType.INTEGER8 ->
                CandidValue.Integer8(source.readByte())

            CandidPrimitiveType.INTEGER16 ->
                CandidValue.Integer16(source.readShortLe())

            CandidPrimitiveType.INTEGER32 ->
                CandidValue.Integer32(source.readIntLe())

            CandidPrimitiveType.INTEGER64 ->
                CandidValue.Integer64(source.readLongLe())

            CandidPrimitiveType.FLOAT32 ->
                CandidValue.Float32(Float.fromBits(source.readIntLe()))

            CandidPrimitiveType.FLOAT64 ->
                CandidValue.Float64(Double.fromBits(source.readLongLe()))

            CandidPrimitiveType.TEXT -> {
                val text = readString(source)
                CandidValue.Text(text)
            }

            CandidPrimitiveType.RESERVED -> CandidValue.Reserved
            CandidPrimitiveType.EMPTY -> CandidValue.Empty

            CandidPrimitiveType.PRINCIPAL -> {
                val present = source.readByte().toInt() == 1
                if (present) {
                    val n = LEB128.decodeUnsigned<Int>(source)
                    val bytes = source.readByteArray(n.toLong())
                    CandidValue.Principal(CandidPrincipal(bytes))
                } else {
                    CandidValue.Principal(null)
                }
            }

            else -> throw CandidDeserializationError.InvalidPrimitive()
        }
    }

    private fun decodeTypeTableValue(
        typeRef: Int,
        source: BufferedSource,
        table: CandidDecodableTypeTable
    ): CandidValue {
        return when (val type = table.tableData[typeRef]) {

            is CandidTypeTableData.Option -> {
                val present = source.readByte().toInt() == 1
                if (present) {
                    CandidValue.Option(decodeValue(type.containedType, source, table))
                } else {
                    CandidValue.Option(table.getTypeForReference(type.containedType))
                }
            }

            is CandidTypeTableData.Vector -> {
                val n = LEB128.decodeUnsigned<Int>(source)
                val items = (0 until n).map {
                    decodeValue(type.containedType, source, table)
                }

                if (type.containedType == CandidPrimitiveType.NATURAL8.value) {
                    CandidValue.Blob(
                        items.mapNotNull { it.natural8Value }
                            .map { it.toByte() }
                            .toByteArray()
                    )
                } else if (items.isEmpty()) {
                    CandidValue.Vector(table.getTypeForReference(type.containedType))
                } else {
                    CandidValue.Vector(CandidVector(items))
                }
            }

            is CandidTypeTableData.Record -> {
                val map = hashMapOf<Long, CandidValue>()
                type.rows.forEach { row ->
                    map[row.hashedKey] = decodeValue(row.type, source, table)
                }
                CandidValue.Record(CandidRecord(map))
            }

            is CandidTypeTableData.Variant -> {
                val index = LEB128.decodeUnsigned<Int>(source)
                val row = type.rows[index]
                CandidValue.Variant(
                    CandidVariant(
                        candidTypesList = type.rows.map {
                            CandidKeyedType(
                                key = it.hashedKey,
                                type = table.getTypeForReference(it.type)
                            )
                        },
                        value = decodeValue(row.type, source, table),
                        valueIndex = index.toULong()
                    )
                )
            }

            is CandidTypeTableData.Function -> {
                val present = source.readByte().toInt() == 1
                val method = if (present) {
                    require(source.readByte().toInt() == 1) {
                        throw CandidDeserializationError.InvalidTypeReference()
                    }
                    val principalLen = LEB128.decodeUnsigned<Int>(source)
                    val principalBytes = source.readByteArray(principalLen.toLong())
                    val name = readString(source)
                    ServiceMethod(name, CandidPrincipal(principalBytes))
                } else null

                val signature = table.getTypeForReference(typeRef).functionSignature
                    ?: throw RuntimeException("serviceSignature must be not null")

                CandidValue.Function(CandidFunction(signature, method))
            }

            is CandidTypeTableData.Service -> {
                val present = source.readByte().toInt() == 1
                val principal = if (present) {
                    val len = LEB128.decodeUnsigned<Int>(source)
                    CandidPrincipal(source.readByteArray(len.toLong()))
                } else null

                val signature = table.getTypeForReference(typeRef).serviceSignature
                    ?: throw RuntimeException("serviceSignature must be not null")

                CandidValue.Service(CandidService(principal, signature))
            }
        }
    }

    fun readString(source: BufferedSource): String {
        val length = LEB128.decodeUnsigned<Int>(source)
        val bytes = source.readByteArray(length.toLong())
        return bytes.decodeToString()
    }
}
