package com.dn.vi.ext.view

import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieListener
import com.airbnb.lottie.LottieTask
import com.dn.vi.app.base.app.AppMod
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

/**
 * 本地的Lottie 包
 * Created by holmes on 2020/5/21.
 **/
data class LottieAnim(val name: String, val imageDir: String)


/**
 *  自己控制 的Lottie加载器
 * Created by holmes on 2020/5/21.
 **/
class RxLottieLoader(private val anim: LottieAnim) : Observable<LottieComposition>() {

    override fun subscribeActual(observer: Observer<in LottieComposition>) {
        val d = D(observer)
        observer.onSubscribe(d)
        d.run()
    }

    private inner class D(val observer: Observer<in LottieComposition>) :
        MainThreadDisposable(), Runnable {

        private var task: LottieTask<LottieComposition>? = null
        val listener = object : LottieListener<LottieComposition> {
            override fun onResult(result: LottieComposition?) {
                if (result == null) {
                    if (!isDisposed) {
                        observer.onError(NullPointerException("load null anim"))
                    }
                    return
                }
                if (!isDisposed) {
                    observer.onNext(result)
                    observer.onComplete()
                }
                task?.removeListener(this)
            }
        }

        override fun run() {
            val task = LottieCompositionFactory.fromAsset(AppMod.app, anim.name)
            task.addListener(listener)
        }

        override fun onDispose() {
            task?.removeListener(listener)
        }

    }
}
