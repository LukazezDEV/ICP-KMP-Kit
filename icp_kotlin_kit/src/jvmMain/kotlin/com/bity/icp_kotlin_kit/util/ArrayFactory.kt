package com.bity.icp_kotlin_kit.util

import kotlin.reflect.KClass

actual object ArrayFactory {
    actual fun <T : Any> newArray(component: KClass<T>, length: Int): Any {
        return java.lang.reflect.Array.newInstance(component.java, length)
    }
}