package com.kingja.loadsir.core

import android.content.Context
import android.os.Looper
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback

/**
 * @author ljkeo
 * @date 2022/9/5 13:56
 */
class LoadLayout(
    context: Context,
    private val onReload: OnReloadListener?
) : FrameLayout(context) {
    private val callbackPool: MutableMap<Class<out Callback?>?, Callback?> = HashMap()

    private var preCallback: Class<out Callback?>? = null
    var currentCallback: Class<out Callback?>? = null
        private set

    fun setupSuccessLayout(callback: Callback) {
        addCallback(callback)
        val successView = callback.getRootView()
        successView.visibility = INVISIBLE
        addView(
            successView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        currentCallback = SuccessCallback::class.java
    }

    fun setupCallback(callback: Callback) {
        callback.copy().let {
            it.setCallback(context, onReload)
            addCallback(it)
        }
    }

    fun addCallback(callback: Callback) {
        callbackPool.getOrPut(callback.javaClass) { callback }
    }

    fun showCallback(callback: Class<out Callback?>) {
        checkCallbackExist(callback)
        if (isMainThread) {
            showCallbackView(callback)
        } else {
            postToMainThread(callback)
        }
    }

    private fun postToMainThread(status: Class<out Callback?>?) {
        post { showCallbackView(status) }
    }

    private fun showCallbackView(status: Class<out Callback?>?) {
        if (preCallback != null) {
            if (preCallback == status) {
                return
            }
            callbackPool[preCallback]!!.onDetach()
        }
        if (childCount > 1) {
            removeViewAt(CALLBACK_CUSTOM_INDEX)
        }
        for (key in callbackPool.keys) {
            if (key == status) {
                val successCallback = callbackPool[SuccessCallback::class.java] as SuccessCallback
                if (key == SuccessCallback::class.java) {
                    successCallback.isShow(true)
                } else {
                    val instance = callbackPool[key]
                    successCallback.isShow(instance!!.getSuccessVisible())
                    val rootView = instance.getRootView()
                    addView(rootView)
                    instance.onAttach(context, instance.actualRootView)
                }
                preCallback = status
            }
        }
        currentCallback = status
    }

    fun setCallBack(callback: Class<out Callback?>, transport: TransportKt?) {
        checkCallbackExist(callback)
        transport?.invoke(callbackPool[callback]!!.obtainRootView())
    }

    private fun checkCallbackExist(callback: Class<out Callback?>?) {
        require(callbackPool.containsKey(callback)) {
            String.format(
                "The Callback (%s) is nonexistent.", callback?.simpleName
            )
        }
    }

    companion object {
        private const val CALLBACK_CUSTOM_INDEX = 1
    }
}

internal val isMainThread: Boolean
    get() = Looper.myLooper() == Looper.getMainLooper()
