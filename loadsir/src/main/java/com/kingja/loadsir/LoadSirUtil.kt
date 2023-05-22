package com.kingja.loadsir

import android.os.Looper
import com.kingja.loadsir.target.ITarget

/**
 * Description:TODO
 * Create Time:2017/9/4 16:24
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
object LoadSirUtil {
    val isMainThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()

    fun getTargetContext(target: Any, targetContextList: List<ITarget>): ITarget {
        for (targetContext in targetContextList) {
            if (targetContext == target) {
                return targetContext
            }
        }
        throw IllegalArgumentException("No TargetContext fit it")
    }
}