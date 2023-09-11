package com.kingja.loadsir.callback

/**
 * Description:TODO
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
class SimpleCallback(private val layoutRes: Int) : Callback() {
    override fun onCreateView(): Int = layoutRes
}
