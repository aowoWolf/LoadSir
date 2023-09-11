package com.kingja.loadsir.core

import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.target.ActivityTarget
import com.kingja.loadsir.target.ITarget
import com.kingja.loadsir.target.ViewTarget

/**
 * Description:TODO
 * Create Time:2017/9/2 16:36
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
class LoadSir private constructor() {
    private var mBuilder: Builder = Builder()

    private constructor(builder: Builder) : this() {
        mBuilder = builder
    }

    private fun setBuilder(builder: Builder) {
        this.mBuilder = builder
    }


    fun register(target: Any, onReload: OnReloadListener? = null): LoadService<*> {
        return register<Any>(target, onReload, null)
    }

    fun <T> register(
        target: Any,
        onReload: OnReloadListener?,
        convertor: Convertor<T>? = null
    ): LoadService<*> {
        val targetContext = getTargetContext(target, mBuilder.getTargetContextList())
        val loadLayout = targetContext.replaceView(target, onReload)
        return LoadService(convertor, loadLayout, mBuilder)
    }

    class Builder {
        private val callbacks: MutableList<Callback> = ArrayList()
        private val targetContextList: MutableList<ITarget> = ArrayList()
        var defaultCallback: Class<out Callback>? = null
            private set

        init {
            targetContextList.add(ActivityTarget())
            targetContextList.add(ViewTarget())
        }

        fun addCallback(callback: Callback) = apply {
            callbacks.add(callback)
        }

        /**
         * @param targetContext
         * @return Builder
         * @since 1.3.8
         */
        fun addTargetContext(targetContext: ITarget) = apply {
            targetContextList.add(targetContext)
        }

        fun getTargetContextList(): List<ITarget> = targetContextList

        fun setDefaultCallback(defaultCallback: Class<out Callback>) = apply {
            this.defaultCallback = defaultCallback
        }

        fun getCallbacks(): List<Callback> = callbacks

        fun commit() {
            INSTANCE.setBuilder(this)
        }

        fun build(): LoadSir = LoadSir(this)
    }

    companion object {
        private val INSTANCE by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LoadSir() }
        fun getDefault(): LoadSir = INSTANCE

        fun beginBuilder(): Builder = Builder()
    }
}

internal fun getTargetContext(target: Any, targetContextList: List<ITarget>): ITarget {
    return targetContextList.find { it == target }
        ?: throw IllegalArgumentException("No TargetContext fit it")
}
