package com.mckj.template.abs

import android.content.Context
import android.view.View

/**
 * @author xx
 * @version 1
 * @createTime 2021/8/31 10:46
 * @desc
 */
abstract class BaseViewContainer<T> {
    var context: Context
        protected set
    open var rootView: View

    var data: T? = null
    constructor(context: Context) {
        this.context = context
        rootView = initView(this.context)
    }

    @JvmName("setData1")
    fun setData(data: T) {
        this.data = data
        updateUI(context, data)
    }

    protected abstract fun initView(context: Context): View
    protected abstract fun updateUI(context: Context, data: T)
}