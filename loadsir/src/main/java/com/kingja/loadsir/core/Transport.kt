package com.kingja.loadsir.core

import android.content.Context
import android.view.View

/**
 * Description:TODO
 * Create Time:2017/9/28 6:45
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
interface Transport {
    fun order(context: Context?, view: View?)
}

typealias TransportKt = (context: Context?, view: View?)->Unit