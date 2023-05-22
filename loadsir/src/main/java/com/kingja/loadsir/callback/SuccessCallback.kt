package com.kingja.loadsir.callback

import android.content.Context
import android.view.View

/**
 * Description:TODO
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
class SuccessCallback(view: View?, context: Context?, onReloadListener: OnReloadListener?) :
    Callback(view, context, onReloadListener) {
    override fun onCreateView(): Int {
        return 0
    }

    @Deprecated("Use {@link #showWithCallback(boolean successVisible)} instead.")
    fun hide() {
        obtainRootView().visibility = View.INVISIBLE
    }

    fun show() {
        obtainRootView().visibility = View.VISIBLE
    }

    fun showWithCallback(successVisible: Boolean) {
        obtainRootView().visibility = if (successVisible) View.VISIBLE else View.INVISIBLE
    }
}