package com.dn.vi.app.base.arch.gmvp.kt

import androidx.lifecycle.LifecycleOwner
import com.dn.vi.app.base.arch.gmvp.BasePresenter
import com.dn.vi.app.base.arch.gmvp.BaseView
import com.dn.vi.app.base.arch.gmvp.LifecycleView
import com.dn.vi.app.base.arch.gmvp.LoadingView
import com.dn.vi.app.base.arch.gmvp.ext.PresenterLifecycleBinder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

/**
 * mvp的 扩展
 * Created by holmes on 2020/5/22.
 **/


/**
 * 与[lifecycleOwner]关联。
 * 在[onCreate]的时候，执行 [BasePresenter.subscribe]
 * 在[onDestroy]的时候，执行 [BasePresenter.unsubscribe]
 */
fun BasePresenter.associateWith(lifecycleOwner: LifecycleOwner) {
    PresenterLifecycleBinder(lifecycleOwner, this)
}

/**
 * 将presenter attach到对应的 BaseView上
 */
fun <T : BasePresenter> T.attach(view: BaseView<T>) {
    view.setPresenter(this)
}

/**
 * 将LoadingView 关联到 Rx上
 *
 * [get()]表示，是否已出错
 */
class LoadingViewRxDelegate(loadingView: LoadingView) : AtomicBoolean(false) {

    private val ref: WeakReference<LoadingView> = WeakReference(loadingView)

    private val onStart: Consumer<in Disposable> = Consumer { disposable: Disposable ->
        ref.get()?.startLoading()
    }

    private val onError: Consumer<in Throwable> = Consumer { err: Throwable ->
        if (compareAndSet(false, true)) {
            ref.get()?.stopLoading(true, err)
        }
    }

    private val onFinally: Action = Action {
        if (!get()) {
            ref.get()?.stopLoading(false, null)
        } else {
            // error
        }
    }

    /**
     * 关联上RxObservable
     */
    fun <T> connectObservable(observable: Observable<T>): Observable<T> {
        return observable.doOnSubscribe(onStart)
            .doOnError(onError)
            .doFinally(onFinally)
    }

}

/**
 * 将RxObservable与 LoadingView 关联上
 */
fun <T> Observable<T>.connectLoading(loadingView: LoadingView): Observable<T> {
    val delegate = LoadingViewRxDelegate(loadingView)
    return delegate.connectObservable(this)
}

/**
 * 将RxObservable与 LoadingView 关联上
 */
fun <T> Observable<T>.bindLifecycle(lifecycle: LifecycleView): Observable<T> {
    return this.compose(lifecycle.bindUntilDestroy())
}