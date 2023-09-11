package com.kingja.loadsir.target

import android.view.View
import android.view.ViewGroup
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadLayout
import com.kingja.loadsir.core.OnReloadListener

/**
 * Description:TODO
 * Create Time:2019/8/29 0029 下午 2:44
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
class ViewTarget : ITarget {
    override fun equals(target: Any?): Boolean = target is View

    override fun replaceView(
        target: Any,
        reloadListener: OnReloadListener?
    ): LoadLayout {
        val oldContent = target as View
        val contentParent = oldContent.parent as ViewGroup
        var childIndex = 0
        val childCount = contentParent.childCount
        for (i in 0 until childCount) {
            if (contentParent.getChildAt(i) === oldContent) {
                childIndex = i
                break
            }
        }
        contentParent.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(oldContent.context, reloadListener)
        loadLayout.setupSuccessLayout(
            SuccessCallback(
                oldContent,
                oldContent.context,
                reloadListener
            )
        )
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}