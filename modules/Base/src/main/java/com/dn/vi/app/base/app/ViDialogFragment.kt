package com.dn.vi.app.base.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.lifecycleScope
import com.dn.vi.app.base.arch.gmvp.LifecycleView
import com.dn.vi.app.base.lifecycle.RxLifecycleDelegate
import com.dn.vi.app.base.view.removeFromParent
import kotlinx.coroutines.CoroutineScope

/**
 *
 * Dialog Fragment
 *
 * Created by holmes on 18-3-21.
 */
abstract class ViDialogFragment : AppCompatDialogFragment(), LifecycleView, NamedPage {

    /**
     * 与Fragment绑定的scope
     * 当Fragment destroy后会被cancel
     */
    val scope: CoroutineScope
        get() = lifecycleScope

    /**
     * 与activity绑定的scope
     * 当activity destroy后会被cancel
     */
    val activityScope: CoroutineScope?
        get() = (activity as? ViActivity)?.scope

    protected var root: View? = null

    /**
     * 一个标识位，具体做用看具体的子类实现
     */
    protected var needRefreshView = false

    private val rxLifecycle: RxLifecycleDelegate by lazy { RxLifecycleDelegate(this) }

    override fun getPageName(): String {
        return ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val r = root
        if (r != null) {
            r.removeFromParent()
            return r
        }
        root = onCreateRootView(inflater, container, savedInstanceState)
        return root
    }

    abstract fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    override fun show(manager: androidx.fragment.app.FragmentManager, tag: String?) {
        if (!isAdded) {
            super.show(manager, tag)
        }
    }

    override fun show(transaction: androidx.fragment.app.FragmentTransaction, tag: String?): Int {
        if (!isAdded) {
            return super.show(transaction, tag)
        }
        return 0
    }

    override fun showNow(manager: androidx.fragment.app.FragmentManager, tag: String?) {
        if (!isAdded) {
            super.showNow(manager, tag)
        }
    }

    inline fun <reified A> activityAs(block: A.() -> Unit) {
        (activity as? A)?.block()
    }

    // === Lifecycle ===

    override fun <T> bindUntilDestroy(): RxLifecycleDelegate.LifecycleTransformer<T> {
        return rxLifecycle.bindUntilDestroy()
    }

    // === ===


}