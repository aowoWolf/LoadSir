package com.kingja.loadsir.target

import android.R
import android.app.Activity
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
class ActivityTarget : ITarget {
    override fun equals(target: Any?): Boolean = target is Activity

    override fun replaceView(
        target: Any,
        onReload: OnReloadListener?
    ): LoadLayout {
        val activity = target as Activity
        val contentParent = activity.findViewById<ViewGroup>(R.id.content)
        val childIndex = 0
        val oldContent = contentParent.getChildAt(childIndex)
        contentParent.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(activity, onReload)
        loadLayout.setupSuccessLayout(SuccessCallback(oldContent, activity, onReload))
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}