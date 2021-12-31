package com.dn.vi.app.cm.utils

import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable


/**
 * 带有效期的数据
 * @param internal 如果为0 则不过期
 * Created by holmes on 17-11-9.
 */
open class IntervalData<T>(internal: Long) {

    private var data: T? = null
        @Synchronized get
        @Synchronized set

    private var interval: Long = if (internal > 0L) internal else 0L
    private var putTime: Long = 0L

    fun get(): T? {
        if (isOutDate()) {
            data = null
            return null
        }
        return data
    }

    /**
     * 获取最后一个值。
     * 注：包括最后一个已经过期的, 并会清掉最后这个过期值
     */
    fun getLast(): T? {
        if (isOutDate()) {
            val old = data
            data = null
            return old
        }
        return data
    }

    fun set(data: T?) {
        this.data = data
        putTime = System.currentTimeMillis()
    }

    open fun isOutDate(): Boolean {
        if (interval == 0L) {
            return false
        }
        return Math.abs(System.currentTimeMillis() - putTime) > interval
    }


}

/**
 * rx的带有效期的数据缓存
 *
 * 数据加载通过 [builder]
 *
 * 使用 [rxGet], 一般不要直接用 [get]
 */
class RxIntervalCache<T>(internal: Long, val builder: RxCreatorBuilder<T>) : IntervalData<T>(internal) {

    interface RxCreatorBuilder<T> {
        /**
         * 返回 rx的数据加载
         */
        fun build(): Observable<T>
    }

    companion object {

        /**
         * create a builder with lambda
         */
        inline fun <reified T> builder(crossinline b: () -> Observable<T>): RxCreatorBuilder<T> {
            return object : RxCreatorBuilder<T> {
                override fun build(): Observable<T> = b()
            }
        }
    }

    /**
     * get with Rx
     */
    fun rxGet(): Observable<T> {
        val data = get()
        if (data != null) {
            return Observable.just(data)
        }
        return OBDelegate<T>(builder.build())
                .doOnNext { set(it) }
    }

    private class OBDelegate<T>(val origin: Observable<T>) : Observable<T>() {

        override fun subscribeActual(observer: Observer<in T>) {
            val d = D(observer)
            observer.onSubscribe(d)
            d.run()
        }

        private inner class D(val ob: Observer<in T>) : MainThreadDisposable(), Runnable {

            private var dispose: Disposable? = null

            override fun run() {
                dispose = origin.subscribe(
                        { next ->
                            ob.onNext(next)
                        },
                        { e ->
                            ob.onError(e)
                        },
                        {
                            ob.onComplete()
                        }
                )
            }

            override fun onDispose() {
                dispose?.dispose()
            }
        }

    }

}