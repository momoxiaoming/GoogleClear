package com.dn.vi.app.cm.entity

import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Describe:第一个observer时，不返回数据
 *
 * Created By yangb on 2021/5/29
 */
class SingleObserver<T>(private val observer: Observer<T>) : Observer<T> {

    private val isFirst = AtomicBoolean(true)

    override fun onChanged(t: T) {
        if (!isFirst.getAndSet(false)) {
            observer.onChanged(t)
        }
    }

}