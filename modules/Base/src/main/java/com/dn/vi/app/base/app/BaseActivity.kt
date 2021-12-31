package com.dn.vi.app.base.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.qq.e.base.IBase
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.atomic.AtomicReference

/**
 *  Base Activity。
 * Created by holmes on 2020/5/19.
 **/
abstract class BaseActivity : AppCompatActivity(), NamedPage, IBase {

    /**
     * 扩展属性中使用.
     * [Activities]
     */
    internal var internalLifeQueue = AtomicReference<Any>()

    /**
     * 是否要设置layout.
     */
    protected var willSetLayout = true
        private set

    /**
     * 绑定activity的lifecycle 的 Coroutine Scope
     */
    val scope: CoroutineScope
        get() = lifecycleScope

    override fun getPageName(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInternalCreate(savedInstanceState)
        willSetLayout = beforeSetLayout()
        if (!willSetLayout) {
            onNotSetLayout()
            return
        }
        initLayout()
    }

    /**
     * 内部的onCreate 基本等同于super.onCreate.
     * 优先于 beforeSetLayout
     * @param savedInstanceState
     */
    protected open fun onInternalCreate(savedInstanceState: Bundle?) {

    }

    /**
     * 在初始化layout前。控制流程。
     * 如果不用设置ui layout， 则返回 false.
     * 如果返回 false (如环境异常), 则会回调 [onNotSetLayout]。
     *
     * 同时将返回值设置到 [willSetLayout]
     */
    protected open fun beforeSetLayout(): Boolean {
        return true
    }

    /**
     * 无须设置layout的时候，会回调。
     * 默认 finish activity
     */
    protected open fun onNotSetLayout() {
        this.finish()
    }

    /**
     * 先执行initLayout后执行onCreate
     */
    protected abstract fun initLayout()

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            performOnFinished()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        performOnFinished()
    }

    private var onFinished = false

    private fun performOnFinished() {
        if (!onFinished) {
            onFinished = true
            onFinishing()
        }
    }

    /**
     * 正在结束。 一般扫尾操作 可以替换 掉 [onDestroy]
     *
     * 如果调用了[finish].
     * 则会在页面关闭时，也就是[onPause]的时候，调用 [onFinishing]。
     * 相比 [onDestroy]会更及时一些。
     *
     * 在有些后台任务时，activity finish后不会马上调用 [onDestroy]。
     */
    protected open fun onFinishing() {
    }

    /**
     * 返回。
     * 默认就是 finish
     */
    open fun goBack() {
        finish()
    }

}