package com.dn.vi.app.cm.kt

import io.reactivex.rxjava3.disposables.Disposable


/**
 * rxjava 相关扩展, 简写等。
 * 完整的使用 查看 https://github.com/ReactiveX/RxKotlin
 * Created by holmes on 2020/5/19.
 **/


/**
 * 直接 取消一个subscription
 */
fun Disposable?.cancelKt() {
    if (this != null && !this.isDisposed) {
        this.dispose()
    }
}


