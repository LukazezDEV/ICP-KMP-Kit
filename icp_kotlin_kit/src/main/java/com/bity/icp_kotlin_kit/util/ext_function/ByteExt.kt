package com.bity.icp_kotlin_kit.util.ext_function

import okio.BufferedSource

fun Byte.Companion.readFrom(source: BufferedSource): Byte =
    source.readByte()