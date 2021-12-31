/**
 * 暴露一些viewModel的方法
 * Created by holmes on 2020/10/19.
 **/
package androidx.lifecycle

import java.io.Closeable

internal const val RX_LIFECYCLE_KEY = "mvvm.RxLifecycle"

/**
 * 设置一个将[ViewModel]的[ViewModel.onClear()]绑定到[RxLifecycle].dipsose()的触发器
 */
internal fun ViewModel.bindRxLifecycleTrigger(closeable: Closeable) {
    setTagIfAbsent(RX_LIFECYCLE_KEY, closeable)
}