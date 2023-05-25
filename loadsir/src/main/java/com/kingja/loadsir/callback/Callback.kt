package com.kingja.loadsir.callback

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
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
    private var onReloadListener: OnReloadListener?
) : Serializable {
    /**
     * if return true, the successView will be visible when the view of callback is attached.
     */
    var successVisible = false
        protected set

    fun setCallback(context: Context?, onReloadListener: OnReloadListener?): Callback {
        this.context = context
        this.onReloadListener = onReloadListener
        return this
    }

    fun getRootView(): View? {
        val resId = onCreateView()
        if (resId == 0 && rootView != null) {
            return rootView
        }
        if (onBuildView(context) != null) {
            rootView = wrapView(onBuildView(context))
        }
        if (rootView == null) {
            rootView = wrapView(View.inflate(context, onCreateView(), null))
        }
        actualRootView.setOnClickListener(View.OnClickListener { v ->
            if (onReloadEvent(context, rootView)) {
                return@OnClickListener
            }

            onReloadListener?.onReload(v)
        })
        //no successCallback
        //onViewCreate(context, rootView);
        onViewCreate(context, actualRootView)
        return rootView
    }

    protected fun wrapView(oldView: View?): View {
        val wrapperView = NestedScrollView(context!!)
        wrapperView.isFillViewport = true
        wrapperView.addView(oldView!!)
        return wrapperView
    }

    protected fun onBuildView(context: Context?): View? = null

    protected fun onReloadEvent(context: Context?, view: View?): Boolean = false

    fun copy(): Callback? {
        val bao = ByteArrayOutputStream()
        var obj: Any?
        ObjectOutputStream(bao).use { it.writeObject(this) }
        ObjectInputStream(ByteArrayInputStream(bao.toByteArray())).use { obj = it.readObject() }
        return obj as Callback?
/*

        val oos: ObjectOutputStream
        try {
            oos = ObjectOutputStream(bao)
            oos.writeObject(this)
            oos.close()
            val bis = ByteArrayInputStream(bao.toByteArray())
            val ois = ObjectInputStream(bis)
            obj = ois.readObject()
            ois.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj as Callback?
*/
    }

    /**
     * @since 1.2.2
     */
    fun obtainRootView(): View {
        if (rootView == null) {
            //no successCallback
            rootView = wrapView(View.inflate(context, onCreateView(), null))
            return actualRootView
        }
        return rootView!!
    }

    val actualRootView: View
        get() = (rootView as ViewGroup?)!!.getChildAt(0)

    interface OnReloadListener : Serializable {
        fun onReload(v: View?)
    }

    protected abstract fun onCreateView(): Int

    /**
     * Called immediately after [.onCreateView]
     *
     * @since 1.2.2
     */
    protected fun onViewCreate(context: Context?, view: View?) {}

    /**
     * Called when the rootView of Callback is attached to its LoadLayout.
     *
     * @since 1.2.2
     */
    fun onAttach(context: Context?, view: View?) {}

    /**
     * Called when the rootView of Callback is removed from its LoadLayout.
     *
     * @since 1.2.2
     */
    fun onDetach() {}
}