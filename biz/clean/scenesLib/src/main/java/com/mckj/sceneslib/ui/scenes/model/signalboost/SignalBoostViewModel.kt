package com.mckj.sceneslib.ui.scenes.model.signalboost

import androidx.core.util.Consumer
import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.data.RamDataRepository
import com.dn.baselib.base.databinding.AbstractViewModel
import com.dn.baselib.ext.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2020/10/21
 */
class SignalBoostViewModel(private val repository: com.mckj.sceneslib.data.RamDataRepository) : AbstractViewModel() {

    companion object {
        const val TAG = "SignalBoostViewModel"
    }

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
            startPercent()
            repository.killAllProcessesToPercent()
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

    override fun onCleared() {
        super.onCleared()
        mCheckJob?.cancel()
        mPercentJob?.cancel()
    }

}