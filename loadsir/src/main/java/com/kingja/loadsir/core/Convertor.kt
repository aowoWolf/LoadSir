package com.kingja.loadsir.core

import com.kingja.loadsir.callback.Callback

/**
 * Description:TODO
 * Create Time:2017/9/4 8:58
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
interface Convertor<T> {
    fun map(t: T): Class<out Callback?>
}