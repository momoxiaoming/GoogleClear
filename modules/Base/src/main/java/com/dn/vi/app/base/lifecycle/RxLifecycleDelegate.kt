package com.dn.vi.app.base.lifecycle

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.drakeet.purewriter.ObscureLifecycleEventObserver
import com.drakeet.purewriter.addObserver
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import org.reactivestreams.Publisher
import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 将Android lifecycle 代理到 rxjava里面
 * 使用效果类似trello的[RxLifecycle]
 *
 *  ```
 *  val rxLifecycle: RxLifecycleDelegate by lazy { RxLifecycleDelegate(lifecycleOwn) }
 *
 *  RxObserver()    // Flowable, Completable
 *  .compose(rxLifeCycle.bindUntilDestroy())
 *  .subscribe(...)
 *
 *  ```
 *
 * Created by holmes on 2020/5/21.
 **/
class RxLifecycleDelegate : Disposable {

    private val disposed = AtomicBoolean(false)

    /**
     *  是否已经disposed
     */
    private val disposedObserver: ObservableBoolean = ObservableBoolean(false)

    /**
     * 把rx的状态与[lifecycleOwner]自动绑定
     */
    constructor(lifecycleOwner: LifecycleOwner) {
        bindLifecycle(lifecycleOwner)
    }

    /**
     * 把rx的dispose状态必须要手动在特定的时候执行[dispose].
     *
     * 一般比如[ViewModel.onCleared];[view.onDetatched];
     * [Activity.onDestroy]这些生命周期方法中调用
     *
     */
    constructor() {
    }

    override fun isDisposed(): Boolean {
        return disposed.get()
    }

    override fun dispose() {
        if (disposed.compareAndSet(false, true)) {
            // cancel all
            disposedObserver.set(true)
        }
    }

    /**
     * 如果要 复用，则调一次 [reset]
     */
    fun reset() {
        if (disposed.compareAndSet(true, false)) {
            disposedObserver.set(false)
        }
    }

    private fun bindLifecycle(lifecycleOwner: LifecycleOwner) {
        bindLifecycle(lifecycleOwner.lifecycle)
    }

    private fun bindLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(LifecycleBinding())
    }

    private inner class LifecycleBinding : ObscureLifecycleEventObserver {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    if (isDisposed) {
                        reset()
                    }
                }
                Lifecycle.Event.ON_DESTROY -> {
                    if (!isDisposed) {
                        dispose()
                    }
                }
                else -> {
                    // nothing
                }
            }
        }

    }

    /**
     * 直接只关联 Destroy
     */
    fun <T> bindUntilDestroy(): LifecycleTransformer<T> {
        return LifecycleTransformer(DisposeDelegate(disposedObserver))
    }

    private class DisposeDelegate(private val disposedObserver: ObservableBoolean) :
        Observable<Boolean>() {

        override fun subscribeActual(observer: Observer<in Boolean>) {
            val dispatcher = DisposedDispatcher(observer)
            observer.onSubscribe(dispatcher)
            dispatcher.run()
        }

        private inner class DisposedDispatcher(val observer: Observer<in Boolean>) :
            MainThreadDisposable(), Runnable {

            private val disposedCallback = object :
                androidx.databinding.Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(
                    sender: androidx.databinding.Observable?,
                    propertyId: Int
                ) {
                    val bool = sender as? ObservableBoolean ?: return
                    if (bool.get()) {
                        disposedObserver.removeOnPropertyChangedCallback(this)

                        if (!isDisposed) {
                            observer.onNext(true)
                            observer.onComplete()
                        }
                    }
                }

            }

            override fun run() {
                disposedObserver.addOnPropertyChangedCallback(disposedCallback)
                if (disposedObserver.get()) {
                    disposedCallback.onPropertyChanged(disposedObserver, 0)
                }
            }

            override fun onDispose() {
                disposedObserver.removeOnPropertyChangedCallback(disposedCallback)
            }

        }

    }

    /**
     * Same from rxLifeCycle
     */
    class LifecycleTransformer<T> internal constructor(observable: Observable<*>) :
        ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>,
        MaybeTransformer<T, T>, CompletableTransformer {

        private val observable: Observable<*>

        override fun apply(upstream: Observable<T>): ObservableSource<T> {
            return upstream.takeUntil(observable)
        }

        override fun apply(upstream: Flowable<T>): Publisher<T> {
            return upstream.takeUntil(observable.toFlowable(BackpressureStrategy.LATEST))
        }

        override fun apply(upstream: Single<T>): SingleSource<T> {
            return upstream.takeUntil(observable.firstOrError())
        }

        override fun apply(upstream: Maybe<T>): MaybeSource<T> {
            return upstream.takeUntil(observable.firstElement())
        }

        override fun apply(upstream: Completable): CompletableSource {
            return Completable.ambArray(upstream, observable.flatMapCompletable {
                Completable.error(CancellationException())
            })
        }

        override fun equals(o: Any?): Boolean {
            if (this === o) {
                return true
            }
            if (o == null || javaClass != o.javaClass) {
                return false
            }
            val that = o as LifecycleTransformer<*>
            return observable == that.observable
        }

        override fun hashCode(): Int {
            return observable.hashCode()
        }

        override fun toString(): String {
            return "LifecycleTransformer{" +
                    "observable=" + observable +
                    '}'
        }

        init {
            this.observable = observable
        }
    }


}