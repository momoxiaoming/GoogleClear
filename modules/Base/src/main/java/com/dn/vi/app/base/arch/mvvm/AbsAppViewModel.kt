package com.dn.vi.app.base.arch.mvvm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.bindRxLifecycleTrigger
import androidx.lifecycle.viewModelScope
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.lifecycle.RxLifecycleDelegate

/**
 * 统一[ViewModel]和[AndroidViewModel]。
 * 带异步操作的生命周期控制工具.
 * rxjava使用[rxLifecycle]控制。
 * coroutine使用[viewModelScope]控制。
 *
 * 如需要dagger的注入，参考`com.dn.vi.app.base.di.ViewModels.kt`
 *
 * Created by holmes on 2020/10/19.
 **/
abstract class AbsAppViewModel : ViewModel() {

    /**
     * 全局application.
     * 同[AppMod.app]
     */
    val app: Application
        get() = AppMod.app

    /**
     * 获取一个指定类似的 [Application]
     */
    inline fun <reified T> appAs(): T {
        return app as T
    }

    /**
     * 与viewModel生命期绑定的 rxLifecycle。
     * 方便自动终止 rxjava的操作
     */
    val rxLifecycle: RxLifecycleDelegate by lazy {
        val rxLife = RxLifecycleDelegate()
        bindRxLifecycleTrigger(CloseableRxLifecycle(rxLife))
        return@lazy rxLife
    }


}