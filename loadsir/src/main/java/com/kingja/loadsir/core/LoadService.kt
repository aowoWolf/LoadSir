package com.kingja.loadsir.core

import android.os.Handler
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback

/**
 * Description:TODO
 * Create Time:2017/9/6 10:05
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
class LoadService<T> internal constructor(
    private val convertor: Convertor<T>?,
    private val loadLayout: LoadLayout,
    builder: LoadSir.Builder
) {

    init {
        builder.getCallbacks().forEach(loadLayout::setupCallback)
        Handler().post { builder.defaultCallback?.let(loadLayout::showCallback) }
    }

    fun showSuccess() {
        loadLayout.showCallback(SuccessCallback::class.java)
    }

    fun showCallback(callback: Class<out Callback?>) {
        loadLayout.showCallback(callback)
    }

    fun showWithConvertor(t: T) {
        requireNotNull(convertor) { "You haven't set the Convertor." }
        loadLayout.showCallback(convertor.map(t))
    }

    val currentCallback: Class<out Callback?>?
        get() = loadLayout.currentCallback

    fun setCallBack(callback: Class<out Callback?>, transport: TransportKt?) = apply {
        loadLayout.setCallBack(callback, transport)
    }
}