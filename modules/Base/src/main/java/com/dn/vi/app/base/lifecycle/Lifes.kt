/**
 * Lifecycle 相关
 * Created by holmes on 2020/5/21.
 **/
package com.dn.vi.app.base.lifecycle

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.drakeet.purewriter.ObscureDefaultLifecycleObserver
import com.drakeet.purewriter.addObserver
import com.drakeet.purewriter.removeObscureObserver


/**
 * 绑定livedata，observeForever. 无视 [lifecycleOwner]的resume状态，
 * 都会有返回值响应。
 *
 * 此方法，只是方便后面[lifecycleOwner]destroy的时候，会自动将[observer]与[LiveData]解绑。
 */
fun <T> LiveData<T>.observeForeverSafe(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    try {
        this.observeForever(observer)
    } catch (e: Exception) {
        return
    }

    val lifecycle = lifecycleOwner.lifecycle
    val defaultLifeOb = object : ObscureDefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            this@observeForeverSafe.removeObserver(observer)
            lifecycle.removeObscureObserver(this)
        }
    }
    lifecycle.addObserver(defaultLifeOb)
}

/**
 * 在主线程设置value的值。
 * 如果当前为主线程，则是直接 [LiveData.setValue]，
 * 否则，则是 [LiveData.postValue]
 */
fun <T> MutableLiveData<T>.setMainValue(v: T?) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        this.value = v
    } else {
        this.postValue(v)
    }
}
