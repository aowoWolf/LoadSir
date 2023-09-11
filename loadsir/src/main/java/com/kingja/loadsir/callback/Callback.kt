package com.kingja.loadsir.callback

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.core.view.get
import androidx.core.widget.NestedScrollView
import com.kingja.loadsir.core.OnReloadListener
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Description:TODO
 * Create Time:2017/9/2 17:04
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
abstract class Callback(
    private var rootView: View?,
    private var context: Context?,
    protected var reloadListener: OnReloadListener?
) : Serializable {
    constructor() : this(null, null, null)

    /**
     * if return true, the successView will be visible when the view of callback is attached.
     */
    open fun getSuccessVisible():Boolean = false

    open fun setCallback(context: Context, reloadListener: OnReloadListener?) = apply {
        this.context = context
        this.reloadListener = reloadListener
    }

    @CallSuper
    open fun getRootView(): View {
        val resId = onCreateView()
        if (resId == 0 && rootView != null) {
            return rootView!!
        }
        if (onBuildView(context) != null) {
            rootView = wrapView(onBuildView(context))
        }
        if (rootView == null) {
            rootView = wrapView(View.inflate(context, onCreateView(), null))
        }
        actualRootView.setOnClickListener {  v->
            if (onReloadEvent(rootView!!)) {
                return@setOnClickListener
            }

            reloadListener?.invoke(v)
        }
        //no successCallback
        //onViewCreate(context, rootView);
        onViewCreate(context, actualRootView)
        return rootView!!
    }

    private fun wrapView(oldView: View?): View =
        NestedScrollView(context!!).apply {
            isFillViewport = true
            addView(oldView!!)
        }

    protected open fun onBuildView(context: Context?): View? = null

    protected open fun onReloadEvent(view: View): Boolean = false

    fun copy(): Callback {
        val bao = ByteArrayOutputStream()
        var obj: Any
        ObjectOutputStream(bao).use { it.writeObject(this) }
        ObjectInputStream(ByteArrayInputStream(bao.toByteArray())).use { obj = it.readObject() }
        return obj as Callback
    }

    /**
     * @since 1.2.2
     */
    fun obtainRootView(): View =
        rootView ?: wrapView(View.inflate(context, onCreateView(), null)).let {
            rootView = it
            actualRootView
        }

    val actualRootView: View
        get() = (rootView as ViewGroup)[0]

/*
    @Deprecated("no used")
    interface OnReloadListener : Serializable {
        fun onReload(v: View?)
    }
*/

    protected abstract fun onCreateView(): Int

    /**
     * Called immediately after [.onCreateView]
     *
     * @since 1.2.2
     */
    protected open fun onViewCreate(context: Context?, view: View?) {}

    /**
     * Called when the rootView of Callback is attached to its LoadLayout.
     *
     * @since 1.2.2
     */
    open fun onAttach(context: Context?, view: View?) {}

    /**
     * Called when the rootView of Callback is removed from its LoadLayout.
     *
     * @since 1.2.2
     */
    open fun onDetach() {}
}