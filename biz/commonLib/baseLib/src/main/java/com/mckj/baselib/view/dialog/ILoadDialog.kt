package com.mckj.baselib.view.dialog

/**
 *
 * 需要自定义[LoadDataBindingFragment,LoadDataBindingActivity]弹窗,必须要实现此接口
 * @author mmxm
 * @date 2021/6/18 14:21
 */
interface ILoadDialog {


    /**
     * 设置内容
     */
    fun setMessage(msg:String?)

    /**
     * 显示
     */
    fun iShow()

    /**
     * 消失
     */
    fun iDismiss()

    /**
     * 是否点击外部消失
     */
    fun setICanceledOnTouchOutside(boolean: Boolean)
}