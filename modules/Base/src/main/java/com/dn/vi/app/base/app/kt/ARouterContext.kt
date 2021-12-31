package com.dn.vi.app.base.app.kt

import android.content.Context
import android.content.ContextWrapper
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

/**
 * arouter和 context的结合体
 * Created by holmes on 2020/10/31.
 **/
class ARouterContext(base: Context) : ContextWrapper(base) {

    val aRouter: ARouter = ARouter.getInstance()

    /**
     * 在[block]填充完成postCard后，
     * 跳转到[path]中
     */
    inline fun navigation(path: String, block: Postcard.() -> Unit): Any? {
        val postCard = aRouter.build(path)
        postCard.block()
        return postCard.navigation(this.baseContext)
    }

}