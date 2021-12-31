package com.dn.vi.app.base.lifecycle

import androidx.lifecycle.MutableLiveData

/**
 * 里面是Resource wrap了的 LiveDate
 *
 * 方便 重构时与 普通 LiveData替换
 *
 * Created by holmes on 2020/7/30.
 **/
open class ResourceLiveData<T> : MutableLiveData<Resource<T>>() {
}