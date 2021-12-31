package com.dn.vi.app.cm.entity

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Describe:
 *
 *
 *
 * Created By yangb on 2020/12/11
 */
class SingleLiveData<T> : MutableLiveData<T> {

    constructor() : super()

    constructor(value: T) : super(value)

    /**
     * @param backFlow 是否防数据倒灌，不接受初始值
     */
    fun observe(backFlow: Boolean, owner: LifecycleOwner, observer: Observer<in T>) {
        if (backFlow) {
            super.observe(owner, SingleObserver(observer))
        } else {
            super.observe(owner, observer)
        }
    }

    fun observeForever(backFlow: Boolean, observer: Observer<in T>) {
        if (backFlow) {
            super.observeForever(SingleObserver(observer))
        } else {
            super.observeForever(observer)
        }
    }

    override fun setValue(value: T) {
        val old = getValue()
        if (value == old) {
            return
        }
        super.setValue(value)
    }

}