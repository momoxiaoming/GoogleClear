package com.dn.vi.app.scaffold

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.*
import com.dn.vi.app.base.app.BaseFragment
import com.dn.vi.app.cm.log.debug
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 可通过rx传反回值的，fragment Transact 的控制器。
 */
abstract class ReactiveFragmentResultObserver<R> : Disposable, LifecycleObserver {

    private var showOb: BtnObserver? = null

    constructor()

    constructor(lifecycleOwner: LifecycleOwner) : this() {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    dispose()
                }
            }
        })
    }

    /**
     * 默认的 取消操作的返回值。
     * 用于通知默认的异常状态
     */
    protected abstract val defaultNegativeValue: R

    /**
     * 添加fragment，并分发fragment的一些[R]状态
     */
    fun rxAdd(
        manager: FragmentManager,
        tag: String?,
        fragment: BaseFragment
    ): Observable<R> {
        val ob = BtnObserver()
        showOb?.d?.dispose()
        showOb = ob
        if (manager.findFragmentByTag(tag) != null) {
            debug { "fragment $tag already shown" }
            dispatchButtonClickObserver(defaultNegativeValue)
            dispose()
            return ob
        }
        manager.commit {
            add(fragment, tag)
        }
        return ob
    }

    /**
     * 显示 [dialog]，并配合监听 button
     */
    fun rxReplace(
        manager: FragmentManager,
        @IdRes ids: Int,
        tag: String?,
        fragment: BaseFragment
    ): Observable<R> {
        val ob = BtnObserver()
        showOb?.d?.dispose()
        showOb = ob
        if (manager.findFragmentByTag(tag) != null) {
            debug { "fragment $tag already shown" }
            dispatchButtonClickObserver(defaultNegativeValue)
            dispose()
            return ob
        }
        manager.commit {
            replace(ids, fragment, tag)
        }
        return ob
    }

    /**
     * 显示 [dialog]，并配合监听 button
     */
    fun rxShow(
        manager: FragmentManager,
        tag: String?,
        dialog: AppCompatDialogFragment
    ): Observable<R> {
        val ob = BtnObserver()
        showOb?.d?.dispose()
        showOb = ob
        if (manager.findFragmentByTag(tag) != null) {
            debug { "dialog $tag already shown" }
            dispatchButtonClickObserver(defaultNegativeValue)
            dispose()
            return ob
        }
        dialog.show(manager, tag)
        return ob
    }

    override fun isDisposed(): Boolean {
        return showOb?.d?.isDisposed ?: true
    }

    override fun dispose() {
        showOb?.d?.apply {
            completed()
            dispose()
        }
        debug { "BIO destroy" }
    }

    /**
     * 如果[rxShow], 则可以用这个来通话点击了什么buttonId
     */
    fun dispatchButtonClickObserver(which: R) {
        showOb?.d?.onButtonClicked(which)
    }

    private inner class BtnObserver : Observable<R>() {

        var d: D? = null

        override fun subscribeActual(observer: Observer<in R>) {
            val d = D(observer)
            this.d = d
            observer.onSubscribe(d)
        }


        inner class D(val observer: Observer<in R>) : MainThreadDisposable() {

            private val completedMark = AtomicBoolean()

            fun onButtonClicked(which: R) {
                if (!isDisposed) {
                    observer.onNext(which)
                }
            }

            fun completed() {
                if (!isDisposed) {
                    if (completedMark.compareAndSet(false, true)) {
                        observer.onComplete()
                    }
                }
            }

            override fun onDispose() {
            }

        }
    }
}

/**
 * 配合fragment使用.简化fragment的返回值
 * 用于 dispatch 分发类似 [DialogInterface.BUTTON_XXX]的消息。
 *
 * Created by holmes on 20-4-11.
 */
class BtnInterfaceObserver :
    ReactiveFragmentResultObserver<Int> {

    constructor() : super()
    constructor(lifecycleOwner: LifecycleOwner) : super(lifecycleOwner)

    override val defaultNegativeValue: Int
        get() = 0

}

class BtnStringInterfaceObserver :
    ReactiveFragmentResultObserver<String> {

    constructor() : super()
    constructor(lifecycleOwner: LifecycleOwner) : super(lifecycleOwner)

    override val defaultNegativeValue: String
        get() = ""

}

