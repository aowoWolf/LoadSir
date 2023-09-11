package com.kingja.loadsir.target

import com.kingja.loadsir.core.LoadLayout
import com.kingja.loadsir.core.OnReloadListener

/**
 * Description:TODO
 * Create Time:2019/8/29 0029 下午 2:43
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
interface ITarget {
    /**
     *
     * @param target
     * @return
     * v1.3.8
     */
    override fun equals(target: Any?): Boolean

    /**
     * 1.removeView 2.确定LP 3.addView
     * @param target
     * @param onReload
     * @return
     * v1.3.8
     */
    fun replaceView(target: Any, onReload: OnReloadListener?): LoadLayout
}