package com.zong.common.utils.coroutine

import com.zong.common.utils.coroutine.Coroutine

internal interface CoroutineContainer {

    fun add(coroutine: Coroutine<*>): Boolean

    fun addAll(vararg coroutines: Coroutine<*>): Boolean

    fun remove(coroutine: Coroutine<*>): Boolean

    fun delete(coroutine: Coroutine<*>): Boolean

    fun clear()

}
