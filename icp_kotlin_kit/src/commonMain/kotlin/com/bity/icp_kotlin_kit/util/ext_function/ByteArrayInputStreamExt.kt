package com.bity.icp_kotlin_kit.util.ext_function

import okio.BufferedSource

fun BufferedSource.readNextBytes(length: Int): ByteArray {
    return readByteArray(length.toLong())
}