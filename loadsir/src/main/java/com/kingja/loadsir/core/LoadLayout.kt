package com.kingja.loadsir.core

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kingja.loadsir.LoadSirUtil
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback

/**
 * @author ljkeo
 * @date 2022/9/5 13:56
 */
class LoadLayout(context: Context, private val onReloadListener: Callback.OnReloadListener?) :
    FrameLayout(context) {
    private val TAG = javaClass.simpleName
    private val callbacks: MutableMap<Class<out Callback?>?, Callback?> = HashMap()
//    private var context: Context? = null

    //    private var onReloadListener: Callback.OnReloadListener? = null
    private var preCallback: Class<out Callback?>? = null
    var currentCallback: Class<out Callback?>? = null
        private set

    /*
        constructor(context: Context, onReloadListener: Callback.OnReloadListener?) : this(context) {
            this.context = context
            this.onReloadListener = onReloadListener
        }
    */

    fun setupSuccessLayout(callback: Callback) {
        addCallback(callback)
        val successView = callback.getRootView()
        successView!!.visibility = INVISIBLE
        addView(
            successView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        currentCallback = SuccessCallback::class.java
    }

    fun setupCallback(callback: Callback?) {
        val cloneCallback = callback!!.copy()
        cloneCallback!!.setCallback(context, onReloadListener)
        addCallback(cloneCallback)
    }

    fun addCallback(callback: Callback?) {
        if (!callbacks.containsKey(callback!!.javaClass)) {
            callbacks[callback.javaClass] = callback
        }
    }

    fun showCallback(callback: Class<out Callback?>?) {
        checkCallbackExist(callback)
        if (LoadSirUtil.isMainThread) {
            showCallbackView(callback)
        } else {
            postToMainThread(callback)
        }
    }

    private fun postToMainThread(status: Class<out Callback?>?) {
        post(Runnable { showCallbackView(status) })
    }

    private fun showCallbackView(status: Class<out Callback?>?) {
        if (preCallback != null) {
            if (preCallback == status) {
                return
            }
            callbacks[preCallback]!!.onDetach()
        }
        if (childCount > 1) {
            removeViewAt(CALLBACK_CUSTOM_INDEX)
        }
        for (key in callbacks.keys) {
            if (key == status) {
                val successCallback = callbacks[SuccessCallback::class.java] as SuccessCallback?
                if (key == SuccessCallback::class.java) {
                    successCallback!!.show()
                } else {
                    val instance = callbacks[key]
                    successCallback!!.showWithCallback(instance!!.successVisible)
                    val rootView = instance.getRootView()
                    addView(rootView)
                    instance.onAttach(context, instance.actualRootView)
                }
                preCallback = status
            }
        }
        currentCallback = status
    }

    fun setCallBack(callback: Class<out Callback?>, transport: Transport) {
        checkCallbackExist(callback)
        transport.order(context, callbacks[callback]!!.obtainRootView())
    }

    fun setCallBack(callback: Class<out Callback?>, transport: TransportKt) {
        checkCallbackExist(callback)
        transport(context, callbacks[callback]!!.obtainRootView())
    }

    private fun checkCallbackExist(callback: Class<out Callback?>?) {
        require(callbacks.containsKey(callback)) {
            String.format(
                "The Callback (%s) is nonexistent.", callback?.simpleName
            )
        }
    }

    companion object {
        private const val CALLBACK_CUSTOM_INDEX = 1
    }
}