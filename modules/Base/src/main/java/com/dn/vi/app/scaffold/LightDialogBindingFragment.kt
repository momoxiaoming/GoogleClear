package com.dn.vi.app.scaffold

/**
 * 通用 默认显示在中间的 Light背景的Dialog
 *
 * 可以通过设置(override)dialog属性来自定义显示效果
 *
 * 多使用 [rxShow]来显示。
 *
 * 子类，在事件上，使用[dispatchButtonClickObserver]。来通知rx
 *
 * Created by holmes on 20-1-11.
 */
abstract class LightDialogBindingFragment : BaseDialogBindingFragment<Int>() {

    override fun bindingBtnInterface(): ReactiveFragmentResultObserver<Int> {
        return BtnInterfaceObserver(this)
    }
}