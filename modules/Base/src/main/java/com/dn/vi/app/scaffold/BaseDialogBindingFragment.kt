package com.dn.vi.app.scaffold

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dn.vi.app.base.app.UI
import com.dn.vi.app.base.app.ViDialogFragment
import com.dn.vi.app.base.view.drawable.SimpleRoundDrawable
import io.reactivex.rxjava3.core.Observable
import org.jetbrains.anko.dip
import kotlin.math.roundToInt

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
abstract class BaseDialogBindingFragment<T> : ViDialogFragment() {

    private var binding: ViewDataBinding? = null


    // === dialog 显示属性设置 ===

    /**
     * dialog的window背景
     */
    protected open val dialogBackgroundDrawable: Drawable by lazy {
        SimpleRoundDrawable(context, 0xffffffff.toInt()).also {
            context?.also { ctx ->
                it.roundRadius = ctx.dip(8).toFloat()
            }
        }
    }

    /**
     * Elevation
     *
     * 0 或 > 0 则有效.
     * -1 为默认值
     */
    protected open val windowElevation: Int = -1

    /**
     * window w:h 比
     *
     * 配合[dialogWindowWidth]，会控制window height的大小
     */
    var aspectRatio: Float = 0F

    /**
     * dialog的width。
     * 默认屏幕左右留36dp
     */
    protected open val dialogWindowWidth: Int
        get() = UI.screenWidth - (context?.dip(72) ?: 72)

    protected open val dialogWindowHeight: Int = WindowManager.LayoutParams.WRAP_CONTENT

    /**
     * 设置显示的gravity。 当不是 [Gravity.NO_GRAVITY]时有效
     */
    protected open val gravity: Int
        get() = Gravity.NO_GRAVITY

    /**
     * 当dialog创建的后。
     * 可以在这里再设置[window]的属性
     */
    protected open fun onDialogWindowCreated(window: Window) {

    }

    // === $ ===


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }

    abstract fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding?

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = onCreateRootBinding(inflater, container)
        this.binding = binding
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindLifecycle()
    }

    protected fun bindLifecycle() {
        Choreographer.getInstance().postFrameCallback {
            binding?.setLifecycleOwner(this)
            onLifecycleBound()
        }
    }


    /**
     * Lifecycle bound on databinding
     */
    protected open fun onLifecycleBound() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.apply {
            unbind()
            // 如果在设置Databinding的数据之前设置 Lifecycle
            // 可能会卡死在 ViewDataBinding.requestRebind
            // 因为里面的 mPendingRebind为true, 但Lifecycle可能还没有start
            // 会造成 mRebindRunnable 无法执行
            setLifecycleOwner(null)
        }
        // currentBinding = null
    }

    /**
     * 清掉dataBinding缓存
     */
    protected fun clearBindingCache() {
        binding?.apply {
            unbind()
            setLifecycleOwner(null)
        }
        binding = null
    }

    private var showOb: ReactiveFragmentResultObserver<T>? = null
        get() {
            if (field == null) {
                field = bindingBtnInterface()
            }
            return field
        }

    abstract fun bindingBtnInterface(): ReactiveFragmentResultObserver<T>

    fun rxShow(manager: FragmentManager, tag: String?): Observable<T> {

        return showOb!!.rxShow(manager, tag, this)
    }

    /**
     * 如果[rxShow], 则可以用这个来通知点击了什么buttonId或者状态事件
     */
    open fun dispatchButtonClickObserver(which: T) {
        showOb?.dispatchButtonClickObserver(which)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return InnerDialog(context, theme)
    }

    /**
     * 自定义dialog， 方便控制属性
     */
    private inner class InnerDialog(context: Context?, theme: Int) :
        AppCompatDialog(context, theme) {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            window?.apply {
                setBackgroundDrawable(dialogBackgroundDrawable)

                val windowWidth = dialogWindowWidth
                var windowHeight = dialogWindowHeight
                if (aspectRatio > 0F && windowWidth > 0) {
                    windowHeight = (windowWidth / aspectRatio).roundToInt()
                }
                setLayout(windowWidth, windowHeight)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    setClipToOutline(true)
                }
                if (windowElevation >= 0) {
                    decorView.elevation = windowElevation.toFloat()
                }

                if (gravity != Gravity.NO_GRAVITY) {
                    setGravity(gravity)
                }

                onDialogWindowCreated(this)
            }
        }

        override fun onStart() {
            super.onStart()
            window?.decorView?.also { decor ->
                decor.setPadding(0, 0, 0, 0)
            }
        }

    }

}