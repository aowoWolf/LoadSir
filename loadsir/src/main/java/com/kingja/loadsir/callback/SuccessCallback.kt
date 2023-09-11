package com.kingja.loadsir.callback

import android.content.Context
import android.view.View
import androidx.core.view.isInvisible
import com.kingja.loadsir.core.OnReloadListener

/**
 * Description:TODO
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
class SuccessCallback(view: View?, context: Context?, onReload: OnReloadListener?) :
    Callback(view, context, onReload) {
    override fun onCreateView(): Int = 0

    fun isShow(isVisible: Boolean) {
        obtainRootView().isInvisible = !isVisible
    }
}