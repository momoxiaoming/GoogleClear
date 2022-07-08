package com.mckj.sceneslib.ui.scenes.model.signalboost

import androidx.lifecycle.MutableLiveData
import com.org.openlib.help.Consumer
import com.mckj.sceneslib.data.RamDataRepository
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Describe:
 *
 * Created By yangb on 2020/10/21
 */
class SignalBoostViewModel(private val repository: com.mckj.sceneslib.data.RamDataRepository) :
    AbstractViewModel() {

    companion object {
        const val TAG = "SignalBoostViewModel"
    }

    var adCached: Boolean = false

    /**
     * 百分比
     */
    val mPercentLiveData = MutableLiveData<Float>()

    private var mCheckJob: Job? = null
    private var mPercentJob: Job? = null

    /**
     * 开始检测
     */
    fun startCheck(consumer: Consumer<Boolean>) {
        mCheckJob?.cancel()
        mCheckJob = launch {
            val startTime = System.currentTimeMillis()

            startPercent()
            repository.killAllProcessesToPercent()

            val countTime = System.currentTimeMillis() - startTime
            val waitAdTime = 8 * 1000L;
            if (countTime < waitAdTime) {
                waitAdLoadEnd(waitAdTime - countTime)
            }

            consumer.accept(true)
        }
    }

    private fun startPercent() {
        mPercentJob?.cancel()
        mPercentJob = launch {
            while (true) {
                delay(50)
                var percent = mPercentLiveData.value ?: 0f
                if (percent <= 99f) {
                    percent += 5f
                }
                if (percent >= 99) {
                    percent = 99f
                }
                mPercentLiveData.value = percent
            }
        }
    }

    /**
     * 等待广告加载结束
     */
    private suspend fun waitAdLoadEnd(timeout: Long = 5000): Boolean {
        return withTimeoutOrNull(timeout) {
            var result = false
            while (true) {
                if (adCached) {
                    result = true
                    break
                }
                delay(100)
            }
            result
        } ?: false
    }

    override fun onCleared() {
        super.onCleared()
        mCheckJob?.cancel()
        mPercentJob?.cancel()
    }

}