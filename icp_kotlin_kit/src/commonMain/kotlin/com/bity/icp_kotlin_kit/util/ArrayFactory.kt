package com.bity.icp_kotlin_kit.util

import kotlin.reflect.KClass

expect object ArrayFactory {
    fun <T : Any> newArray(component: KClass<T>, length: Int): Any
}