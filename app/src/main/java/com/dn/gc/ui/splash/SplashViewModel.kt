package com.dn.gc.ui.splash

import androidx.lifecycle.MutableLiveData
import com.mckj.api.util.ScopeHelper.launch
import com.mckj.baselib.base.databinding.AbstractViewModel

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/6/17
 */
class SplashViewModel : AbstractViewModel() {

    /**
     * 加载时间
     */
    val mLoadTimeLiveData = MutableLiveData<Float>()

    private var mLoadTimeJob: Job? = null

    fun loadTimeout(timeoutMillis: Long = 10 * 1000L) {
        mLoadTimeJob?.cancel()
        mLoadTimeJob = launch {
            val time = System.currentTimeMillis()
            while (true) {
                delay(100)
                val timeout = System.currentTimeMillis() - time
                if (timeout >= timeoutMillis) {
                    break
                }
                mLoadTimeLiveData.value = 100f * timeout / timeoutMillis
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mLoadTimeJob?.cancel()
    }

}